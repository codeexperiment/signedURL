# signed URL

The full context of signed URLs is explained in [**what is a signed URL**](https://abstractprogramming.blogspot.com/2017/10/what-is-signedurl.html).

A signed URL is a regular URL + a signature which can be verified by the server servicing your request, i.e. http://myexampleserver.com/12314?sig=AF830D849.

There are 3 actors:
  - Client
  - Authority server
  - Data server

The client gets the signature from the Authority server and appends it to the URL as a query parameter. 

The full formed URL (signed URL) is then sent to the Data server.

The Data server then verifies the signature using the public key provided by the Authority server.

## Running the code

### Creating they keys
Pick a directory from which to run the following linux commands, i.e. /home/user/keys

Commands are from [codeartisan](http://codeartisan.blogspot.com/2009/05/public-key-cryptography-in-java.html)

Generate the private key
```
openssl genrsa -out private_key.pem 2048
```

Convert the private key to DER format
```
openssl pkcs8 -topk8 -inform PEM -outform DER -in private_key.pem -out private_key.der -nocrypt
```

Generate the public key
```
openssl rsa -in private_key.pem -pubout -outform DER -out public_key.der
```

### Running the Authority server
The Authority server runs on port 8080.

There are 2 arguments to pass in the following order:
- the file location of the public key, i.e. /home/user/keys/publick_key.der
- the file location of the private key, i.e. /home/user/keys/private_key.der

### Running the Data server
The Data server runs on port 8081

The Data server fetches the public key from the Authority server at startup to verify all signatures from any incoming signedURL.

### Running the client
The client makes an http request to the Authority server to fetch the signature for the given dataset ID and then creates the signed URL to send it to the Data server for that specific dataset ID.
