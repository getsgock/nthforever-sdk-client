
开发语言：JAVA
适用JDK版本：1.5及以上

1.下载jar包添加依赖
   sdk-java-common-1.0.jar
   
   pom文件中添加
   
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-lang3</artifactId>
        <version>3.8</version>
    </dependency>
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-io</artifactId>
        <version>1.3.2</version>
    </dependency>
    <dependency>
        <groupId>commons-logging</groupId>
        <artifactId>commons-logging</artifactId>
        <version>1.1.1</version>
        </dependency>
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <version>1.2.49</version>
    </dependency>
2.初始化请求 包括client和request

    //client
    Builder builder = DefaultIpayClient.builder(SERVER_URL, APP_ID, PRIVATE_KEY);
            builder.charset(IpayConstants.CHARSET_UTF8)
                    .encryptType(ENCRYPT_TYPE)
                    .ipayPublicKey(PUBLIC_KEY)
                    .signType(SIGN_TYPE)
                    .encryptKey(ENCRYPT_KEY);
            DefaultIpayClient client = builder.build();
    //此处request和对应的response均由我们提供，商户不需要自己创建
    MyRequest request = new MyRequest();
    //请求所需业务参数
    request.setXXX(Object);
    request.setXXX1(Object1);
    ......
    //执行请求，已完成验签和解密，失败抛异常
    try {
       MyResponse execute = client.execute(request);
       System.out.println(execute);
    } catch (IpayApiException e) {
       e.printStackTrace();
    }
    
