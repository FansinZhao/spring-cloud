默认用户: user

默认密码: Using default security password: XXXX


# 查看用户密码 密码可以
security.oauth2.client.clientId = oauth2-client-id
security.oauth2.client.secret = secret
credentials
# 1 授权端 使用默认用户/密码
## 浏览器
http://localhost:9012/oauth/authorize?response_type=code&client_id=oauth2-client-id&redirect_uri=http://localhost:9012/welcome
http://localhost:9012/oauth/authorize?response_type=code&client_id=oauth2-client-id&redirect_uri=http://localhost:9012/oauth/confirm_access

输入默认用户密码,则url转换为

http://localhost:9012/welcome?code=TtbTEp

## 命令行
curl -v -H "Authorization:Basic YWRtaW46YWRtaW4=" localhost:9012/oauth/authorize -d response_type=code -d client_id=oauth2-client-id -d redirect_uri=http://localhost:9012/welcome
curl -v -H "Authorization:Basic YWRtaW46YWRtaW4=" localhost:9012/oauth/authorize -d response_type=code -d client_id=oauth2-client-id -d redirect_uri=http://localhost:9012/oauth/confirm_access

返回
```
*   Trying 127.0.0.1...
* Connected to localhost (127.0.0.1) port 9012 (#0)
> POST /oauth/authorize HTTP/1.1
> Host: localhost:9012
> User-Agent: curl/7.47.0
> Accept: */*
> Authorization:Basic YWRtaW46YWRtaW4=
> Content-Length: 88
> Content-Type: application/x-www-form-urlencoded
> 
* upload completely sent off: 88 out of 88 bytes
< HTTP/1.1 302 
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: DENY
< Strict-Transport-Security: max-age=31536000 ; includeSubDomains
< X-Application-Context: security-oauth2:server:9012
< Location: `http://localhost:9012/welcome?code=qUFjlo`
< Content-Language: zh-CN
< Content-Length: 0
< Date: Wed, 28 Jun 2017 03:36:48 GMT
< 
* Connection #0 to host localhost left intact
```
转换后的地址是:

    < Location: `http://localhost:9012/welcome?code=qUFjlo`
访问转换后地址:    
curl -H "Authorization:Basic YWRtaW46YWRtaW4="  http://localhost:9012/welcome?code=qUFjlo

返回
    welcome
    
# 2 authorize会调用这个? 确认授权  WhitelabelApprovalEndpoint 参数来源于AuthorizationEndpoint http://localhost:9012/oauth/authorize
curl -H "Authorization:Basic YWRtaW46YWRtaW4=" http://localhost:9012/oauth/confirm_access -d authorizationRequest.clientId=oauth2-client-id


#-----------------------------------------------
    
# 3 获取 Access Token 使用clientId/secret
curl oauth2-client-id:secret@localhost:9012/oauth/token -d grant_type=client_credentials
curl oauth2-client-id:secret@localhost:9012/oauth/token  -d grant_type=client_credentials -d username=user -d password=12345
~~curl oauth2-client-id:secret@localhost:9012/oauth/token  -d grant_type=refresh_token -d refresh_token=615bbfde-21e2-45ea-897f-98db9d133365~~

# 4 校验token 使用clientId/secret
curl oauth2-client-id:secret@localhost:9012/oauth/check_token -d token=e29d6e80-ef22-483e-afe4-7f18a4289502


# 5 错误
/oauth/error

