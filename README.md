# LicenseSever
The server for the License system

## Create a database

Log in to your SQL Client or PHPMyAdmin and type:
```sql
CREATE DATABASE licenseserver
```
You needn't create any tables beacause the server will do it automatically

## Generate Certificates

### Using KeyStoreExplorer (Recommended)

1. Create new Keystore
2. Generate new KeyPair (name it like serverpair)
3. Save the KeyStore (for e.g: ServerKeyStore)
4. Export the Certificate (e.g: serverpair.cert)
5. Create new KeyStore
6. Generate new KeyPair (name it like clientpair)
7. Save the KeyStore (for e.g: ClientKeyStore)
8. Export the Certificate (e.g: clientpair.cert)
9. Create new KeyStore
10. Import the Server Certificate
11. Save it (e.g: ClientTrustStore)
12. Create new KeyStore
13. Import the Client Certificate
14. Save it (e.g: ServerTrustStore)
15. Export the Client and Server private and public keys (e.g: serverpair.pem, serverpair.key, clientpair.pem, serverpair.pem)
16. Make the Client Files public so that the client can download and use it

### Using Console (For experts)

Generate Server Certificate and import it to the Client Trust Store
```
keytool -genkeypair -alias serverpair -keyalg RSA -validity 365 -keysize 2048 -keystore ServerKeyStore
keytool -export -alias serverpair -keystore ServerKeyStore -rfc -file serverpair.cert 
keytool -importcert -alias serverpair -file serverpair.cert -keystore ClientTrustStore
```

Generate Client Certificate and import it to the Server Trust Store
```
keytool -genkeypair -alias clientpair -keyalg RSA -validity 365 -keysize 2048 -keystore ClientKeyStore
keytool -export -alias clientpair -keystore ClientKeyStore -rfc -file clientpair.cert 
keytool -importcert -alias clientpair -file clientpair.cert -keystore ClientKeyStore
```

## Creating the Config file
Copy the config.yml file and change your datas

## Starting the server
Open the console and type

```
java -jar LicenseServer.jar
```
