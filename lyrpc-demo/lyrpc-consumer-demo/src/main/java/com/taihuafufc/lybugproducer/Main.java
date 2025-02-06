package com.taihuafufc.lybugproducer;

public class Main {
    public static void main(String[] args) {
        ReferenceConfig<UserLyrpc> userReferenceConfig = new ReferenceConfig<>();
        userReferenceConfig.setInterfaceClass(UserLyrpc.class);

        ReferenceConfig<ArticleLyrpc> articleReferenceConfig = new ReferenceConfig<>();
        articleReferenceConfig.setInterfaceClass(ArticleLyrpc.class);

        LyrpcBootstrap.getInstance()
                .name("lyrpc-consumer")
                .registry(new RegistryConfig("zookeeper://127.0.0.1:2181"))
                .reference(userReferenceConfig)
                .reference(articleReferenceConfig);

//        // 获取代理对象
//        UserLyrpc userLyrpc = userReferenceConfig.getProxy();
//        String userById = userLyrpc.getUserById(123456);
    }
}