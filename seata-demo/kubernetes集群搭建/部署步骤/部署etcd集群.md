部署etcd集群

安装cfssl工具

~~~powershell
在master节点上操作，安装用于生成证书的工具
# wget https://pkg.cfssl.org/R1.2/cfssl_linux-amd64
# wget https://pkg.cfssl.org/R1.2/cfssljson_linux-amd64
# wget https://pkg.cfssl.org/R1.2/cfssl-certinfo_linux-amd64
# chmod +x cfssl_linux-amd64 cfssljson_linux-amd64 cfssl-certinfo_linux-amd64
# mv cfssl_linux-amd64 /usr/local/bin/cfssl
# mv cfssljson_linux-amd64 /usr/local/bin/cfssljson
# mv cfssl-certinfo_linux-amd64 /usr/local/bin/cfssl-certinfo
~~~

创建etcd部署目录

```powershell
在master节点上操作
# mkdir -pv $HOME/etcd-ssl && cd $HOME/etcd-ssl
```

创建ca-config.json文件

~~~powershell
生成 CA 证书，expiry 为证书过期时间(10 年)
# cat > ca-config.json << EOF
{
  "signing": {
    "default": {
      "expiry": "87600h"
    },
    "profiles": {
      "kubernetes": {
        "expiry": "87600h",
        "usages": [
          "signing",
          "key encipherment",
          "server auth",
          "client auth"
        ]
      }
    }
  }
}
EOF

"expiry": "87600h"   过期时间为10年
~~~

创建etcd-ca-csr.json文件

~~~powershell
生成 CA 证书请求文件， ST/L/字段可自行修改
cat > etcd-ca-csr.json << EOF
{
  "CN": "etcd",
  "key": {
    "algo": "rsa",
    "size": 2048
  },
  "names": [
    {
      "C": "CN",
      "L": "Beijing",
      "ST": "Beijing",
      "O": "etcd",
      "OU": "Etcd Security"
    }
  ]
}
EOF
~~~

创建etcd-csr.json文件

~~~powershell
生成证书请求文件，ST/L/字段可自行修改
cat > etcd-csr.json << EOF
{
  "CN": "etcd",
  "hosts": [
    "127.0.0.1",
    "192.168.0.20",
    "192.168.0.21",
    "192.168.0.22"
  ],
  "key": {
    "algo": "rsa",
    "size": 2048
  },
  "names": [
    {
      "C": "CN",
      "L": "BeiJing",
      "ST": "BeiJing",
      "O": "etcd",
      "OU": "Etcd Security"
    }
  ]
}
EOF
~~~

生成证书

~~~powershell
# 生成 ca.pem ca-key.pem
cd $HOME/etcd-ssl
cfssl gencert -initca etcd-ca-csr.json | cfssljson -bare etcd-ca
# 生成 server.pem server-key.pem
cfssl gencert \
    -ca=etcd-ca.pem \
    -ca-key=etcd-ca-key.pem \
    -config=ca-config.json \
    -profile=kubernetes \
    etcd-csr.json | cfssljson -bare etcd


# 复制证书到部署目录(所有etcd集群节点)
mkdir -pv /data/apps/etcd/{ssl,bin,etc,data}
cp etcd*.pem /data/apps/etcd/ssl
scp -r /data/apps/etcd 192.168.0.21:/data/apps/
scp -r /data/apps/etcd 192.168.0.22:/data/apps/

/data/apps/目录需要提前在其他etcd节点行创建
~~~



下载etcd二进制包

~~~powershell

cd ~
wget https://github.com/etcd-io/etcd/releases/download/v3.3.12/etcd-v3.3.12-linux-amd64.tar.gz
tar zxf etcd-v3.3.12-linux-amd64.tar.gz
cp etcd-v3.3.12-linux-amd64/etcd* /data/apps/etcd/bin/

拷贝到其他节点
scp -r etcd-v3.3.12-linux-amd64/etcd* 192.168.0.21:/data/apps/etcd/bin/
scp -r etcd-v3.3.12-linux-amd64/etcd* 192.168.0.22:/data/apps/etcd/bin/
~~~

创建etcd配置文件

~~~powershell
# 这里的 etcd 虚拟机都有两个网卡，一个用于提供服务，另一个用于集群通信
cat > /data/apps/etcd/etc/etcd.conf << EOF
#[Member]
ETCD_NAME="etcd-01"
ETCD_DATA_DIR="/data/apps/etcd/data/default.etcd"
ETCD_LISTEN_PEER_URLS="https://192.168.0.20:2380"
ETCD_LISTEN_CLIENT_URLS="https://127.0.0.1:2379,https://192.168.0.20:2379"
#
#[Clustering]
ETCD_INITIAL_ADVERTISE_PEER_URLS="https://192.168.0.20:2380"
ETCD_ADVERTISE_CLIENT_URLS="https://127.0.0.1:2379,https://192.168.0.20:2379"
ETCD_INITIAL_CLUSTER="etcd-01=https://192.168.0.20:2380,etcd-02=https://192.168.0.21:2380,etcd-03=https://192.168.0.22:2380"
ETCD_INITIAL_CLUSTER_TOKEN="etcd-cluster"
ETCD_INITIAL_CLUSTER_STATE="new"

#[Security]
ETCD_CERT_FILE="/data/apps/etcd/ssl/etcd.pem"
ETCD_KEY_FILE="/data/apps/etcd/ssl/etcd-key.pem"
ETCD_TRUSTED_CA_FILE="/data/apps/etcd/ssl/etcd-ca.pem"
ETCD_PEER_CERT_FILE="/data/apps/etcd/ssl/etcd.pem"
ETCD_PEER_KEY_FILE="/data/apps/etcd/ssl/etcd-key.pem"
ETCD_PEER_TRUSTED_CA_FILE="/data/apps/etcd/ssl/etcd-ca.pem"
#
[Logging]
ETCD_DEBUG="false"
ETCD_LOG_OUTPUT="default"
EOF

相关参数说明
ETCD_NAME="etcd-01"  定义本服务器的etcd名称
etcd-01,etcd-02,etcd-03 分别为三台服务器上对应ETCD_NAME的值
ETCD_INITIAL_CLUSTER_TOKEN，ETCD_INITIAL_CLUSTER_STATE的值各个etcd节点相同

拷贝到其他节点
scp -r /data/apps/etcd/etc/etcd.conf 192.168.0.21:/data/apps/etcd/etc/
scp -r /data/apps/etcd/etc/etcd.conf 192.168.0.22:/data/apps/etcd/etc/
拷贝完后，修改相关ip地址
~~~

创建etcd.service

~~~powershell
cat > /usr/lib/systemd/system/etcd.service << EOF
[Unit]
Description=Etcd Server
After=network.target
After=network-online.target
Wants=network-online.target
#
[Service]
Type=notify
EnvironmentFile=/data/apps/etcd/etc/etcd.conf
ExecStart=/data/apps/etcd/bin/etcd \\
--name=\${ETCD_NAME} \\
--data-dir=\${ETCD_DATA_DIR} \\
--listen-peer-urls=\${ETCD_LISTEN_PEER_URLS} \\
--listen-client-urls=\${ETCD_LISTEN_CLIENT_URLS} \\
--advertise-client-urls=\${ETCD_ADVERTISE_CLIENT_URLS} \\
--initial-advertise-peer-urls=\${ETCD_INITIAL_ADVERTISE_PEER_URLS} \\
--initial-cluster=\${ETCD_INITIAL_CLUSTER} \\
--initial-cluster-token=\${ETCD_INITIAL_CLUSTER_TOKEN} \\
--initial-cluster-state=\${ETCD_INITIAL_CLUSTER_STATE}
Restart=on-failure
LimitNOFILE=65536
#
[Install]
WantedBy=multi-user.target
EOF

拷贝到其他节点
scp -r /usr/lib/systemd/system/etcd.service 192.168.0.21:/usr/lib/systemd/system/
scp -r /usr/lib/systemd/system/etcd.service 192.168.0.22:/usr/lib/systemd/system/
~~~

启动服务

~~~powershell
useradd -r etcd && chown etcd.etcd -R /data/apps/etcd
systemctl daemon-reload
systemctl enable etcd
systemctl start etcd
systemctl status etcd

~~~

设置环境变量

~~~powershell
echo "PATH=$PATH:/data/apps/etcd/bin/" >> /etc/profile.d/etcd.sh
chmod +x /etc/profile.d/etcd.sh
source /etc/profile.d/etcd.sh

查看集群状态
etcdctl --ca-file=/data/apps/etcd/ssl/etcd-ca.pem --cert-file=/data/apps/etcd/ssl/etcd.pem --key-file=/data/apps/etcd/ssl/etcd-key.pem --endpoints="https://192.168.0.20:2379,https://192.168.0.21:2379,https://192.168.0.22:2379" cluster-health
结果
cluster is degrade(只要有一台有问题就是这种)
cluster is healthy(所以etcd节点都正常)
查看集群成员
ETCDCTL_API=2
etcdctl --endpoints "https://192.168.0.20:2379,https://192.168.0.21:2379,https://192.168.0.22:2379" --ca-file=/data/apps/etcd/ssl/etcd-ca.pem --cert-file=/data/apps/etcd/ssl/etcd.pem --key-file=/data/apps/etcd/ssl/etcd-key.pem member list

ETCDCTL_API=3
etcdctl --endpoints "https://192.168.0.20:2379,https://192.168.0.21:2379,https://192.168.0.22:2379" --cacert=/data/apps/etcd/ssl/etcd-ca.pem --cert=/data/apps/etcd/ssl/etcd.pem --key=/data/apps/etcd/ssl/etcd-key.pem member list

注意：如果没有设置环境变量ETCDCTL_API，则默认使用ETCDCTL_API=2的api
ETCDCTL_API=2与ETCDCTL_API=3对应的命令参数有所不同
~~~





集群启动后出现的错误日志

~~~powershell
the clock difference against peer 97feb1a73a325656 is too high
集群各个节点时钟不同步，通过ntpdate time1.aliyun.com命令可以同步时钟

Dec 24 15:30:25 localhost etcd: health check for peer b3e330dca330e585 could not connect: dial tcp 192.168.0.22:2380: i/o timeout (prober "ROUND_TRIPPER_SNAPSHOT")
Dec 24 15:30:30 localhost etcd: health check for peer b3e330dca330e585 could not connect: dial tcp 192.168.0.22:2380: i/o timeout (prober "ROUND_TRIPPER_SNAPSHOT")
Dec 24 15:30:30 localhost etcd: health check for peer b3e330dca330e585 could not connect: dial tcp 192.168.0.22:2380: i/o timeout (prober "ROUND_TRIPPER_RAFT_MESSAGE")
注意防火墙，selinux的关闭

~~~
