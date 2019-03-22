Elastos.ORG.BlockchainAgent
==============

## Summary

This repo provide simple HTTP Restful API for developers to send did raw data to elastos did side chain.

## Build with maven

In project directory, use maven command:
```Shell
$uname mvn clean compile package
```
If there is build success, Then the package did.chain.agent-0.0.1.jar will be in target directory.

## Configure project properties
In project directory, create configuration file from the template:

```bash
$ pushd src/main/resources
$ cp -v application.properties.in application.properties
$ popd
```

### Configure database
First create database table use sql file in project: block_chain_agent.sql

Change spring.datasource to your database.like:
```yaml
spring.datasource.url=jdbc:mariadb://localhost:3306/up_chain_wallets?useUnicode=true&characterEncoding=UTF-8&useSSL=false
spring.datasource.username=root
spring.datasource.password=12345678
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
```

### Configure did side chain address
Change "node.didPrefix" to your did side chain node url.

### Configure did up chain wallets sum 
Change "wallet.sum" to change the amount of the wallets to be used to up chain. 
the more up chain wallets the more up chain transaction at the same time and the more resource to be used.
So we recommend the number is bigger than 100 and less than 1000.

## Run

Copy did.chain.service-0.0.1.jar to your deploy directory.
then use jar command to run this spring boot application.

```shell
$uname java -jar did.chain.agent-0.0.1.jar
```
## Web Service APIs

### Get deposit address for renewal
```url
/api/v1/upchain/deposit/address
```
All the up chain fee go from deposit wallet, so you will get deposit address to renewal the system.

For example:
We get from our local did chain service request like this:
```url
http://localhost:8093/api/1/blockagent/upchain/deposit/address
```
If Success, we will get response like:
```json
{
    "result": "EZdDnKBRnV8o77gjr1M3mWBLZqLA3WBjB7",
    "status": 200
}
```
The "result" is deposit wallet address, which need to have enough ela(>10ela) to pay the up chain fee. 

### Renewal all the up chain wallets
```url
/api/1/blockagent/upchain/wallets/renewal
```
After you renewal the deposit wallet. You should call this api to renewal the really work wallets(up chain wallet) from the deposit wallet,
to make sure the up chain recording be success.  

For example:
We post to our local did chain service request like this:
```url
http://localhost:8093/api/1/blockagent/upchain/wallets/renewal
```
with header: 
```json
[{"key":"Content-Type","value":"application/json","description":"","enabled":true}]
```
and put the transfer ela in body.
```json
{
    "ela": "10.0"
}
```
If Success, we will get response like:
```json
{
    "result": "a8965892e49a9f285bfd61059b9766b54f98cd0f53bf0548f17860acc9a71964",
    "status": 200
}
```
The "result" is txid, which is the transaction id of deposit wallet transfer to up chain wallets.

### Up raw data to side chain
```yaml
HTTP: POST
URL: /api/1/blockagent/upchain/data
HEADERS:
    Content-Type: application/json
    X-Elastos-Agent-Auth: {"auth":"auth_code","id":"org.elastos.bless.star","time":"system time(long)"}
data: {
    "msg":"7B22646964223A22696A5950654E51354B336B624A6D5545486D566153345439566F5350694634585164222C22646964537461747573223A224E6F726D616C222C2270726F7065727479223A7B226B6579223A226D795F6E6F7465626F6F6B73222C22737461747573223A224E6F726D616C222C2276616C7565223A225B5C2244656C6C5C222C5C224D61635C222C5C225468696E6B7061645C225D227D2C22746167223A224449442050726F7065727479222C2276657273696F6E223A22312E30227D",
    "sig":"92E40A61AFB297C8B7AA97E27DF20B661507C869BA5A7E5F7A08E84791B5100AE4B370E6669F833865223DC2A2D645BECC199CFC31B1A55DA92C0B0E40C09455",
    "pub":"022839482C0D6A844C817F6AEACDD0BC6141A9067105292E8DB024C5A3E78D7C9C"
    }
return:
    成功：{"status":200, "result":{"txid":"b58683d618be1f16cc61f1ade095b2082f9c5e15812dd188de20e7a336d2ae35"}};
    失败:{"status":400, "result":"Err msg"}
```

For example:
I have raw data (which can be created by API:[ElaDidService.packDidRawData](https://did-client-java-api.readthedocs.io/en/latest/did_service_api_guide/#packdidrawdata)):
```json {"msg":"7B22646964223A22696A5950654E51354B336B624A6D5545486D566153345439566F5350694634585164222C22646964537461747573223A224E6F726D616C222C2270726F7065727479223A7B226B6579223A226D795F6E6F7465626F6F6B73222C22737461747573223A224E6F726D616C222C2276616C7565223A225B5C2244656C6C5C222C5C224D61635C222C5C225468696E6B7061645C225D227D2C22746167223A224449442050726F7065727479222C2276657273696F6E223A22312E30227D","sig":"92E40A61AFB297C8B7AA97E27DF20B661507C869BA5A7E5F7A08E84791B5100AE4B370E6669F833865223DC2A2D645BECC199CFC31B1A55DA92C0B0E40C09455","pub":"022839482C0D6A844C817F6AEACDD0BC6141A9067105292E8DB024C5A3E78D7C9C"}
```
Then we post to our local did chain service request like this:
```url
http://localhost:8093/api/1/blockagent/upchain/data
```
with header like: 
```yaml
Content-Type: application/json
X-Elastos-Agent-Auth: {"auth":"c5b50c5b9f1445d8d894cd63e296e4f4", "id":"unCZRceA8o7dbny", "time":"1551169928000"}
```
We Create HTTP header "X-Elastos-Agent-Auth" lile:
```java
import org.apache.shiro.crypto.hash.SimpleHash;
String createAuthHeaderValue(){
    String acc_id = "unCZRceA8o7dbny"; //Get from elastos service platform access key
    String acc_secret = "qtvb4PlRVGLYYYQxyLIo3OgyKI7kUL"; //Get from elastos service platform access key
    long time = new Date().getTime();
    String strTime = String.valueOf(time);
    SimpleHash hash = new SimpleHash("md5", acc_secret, strTime, 1);
    String auth = hash.toHex();
    Map<String, String> map = new HashMap<>();
    map.put("id", acc_id);
    map.put("time", String.valueOf(time));
    map.put("auth", auth);
    String X-Elastos-Agent-Auth_value = JSON.toJSONString(map);
    return X-Elastos-Agent-Auth_value;
}
```

and the raw data in post body of curse.

If Success, we will get response like:
```json
{
    "result": "a8965892e49a9f285bfd61059b9766b54f98cd0f53bf0548f17860acc9a71964",
    "status": 200
}
```
The "result" is txid, which is the record transaction id.

### Get rest of wallets 
```url
/api/v1/upchain/rest
```
Get rest of deposit and working wallets.

For example:
We get from our local did chain service request like this:
```url
http://localhost:8093/api/1/blockagent/upchain/rest
```
If Success, we will get response like:

```json
{
    "result": {
        "DepositRest": 9.999597,
        "WorkingWalltesRest": 11.099888999999978
    },
    "status": 200
}
```
