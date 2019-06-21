package com.my.chen.fabric.mc.user;

import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.util.Collection;
import java.util.Properties;

/**
 * @author chenwei
 * @version 1.0
 * @date 2019/6/14
 * @description
 */
public class App {

    String ccName="mycc";
    String version="1.0";
    String path="chaincode/ecdspay/";
    public static HFClient client = null;
    public static Peer peer = null;
    public static Channel channel = null;
    public static Orderer orderer = null;
    public static String adminPem = "-----BEGIN CERTIFICATE-----\n" +
            "MIICCTCCAbCgAwIBAgIQMSl5/o63oXNVbJ2xbTStDTAKBggqhkjOPQQDAjBpMQsw\n" +
            "CQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEWMBQGA1UEBxMNU2FuIEZy\n" +
            "YW5jaXNjbzEUMBIGA1UEChMLZXhhbXBsZS5jb20xFzAVBgNVBAMTDmNhLmV4YW1w\n" +
            "bGUuY29tMB4XDTE5MDYyMTA5MjIwMFoXDTI5MDYxODA5MjIwMFowVjELMAkGA1UE\n" +
            "BhMCVVMxEzARBgNVBAgTCkNhbGlmb3JuaWExFjAUBgNVBAcTDVNhbiBGcmFuY2lz\n" +
            "Y28xGjAYBgNVBAMMEUFkbWluQGV4YW1wbGUuY29tMFkwEwYHKoZIzj0CAQYIKoZI\n" +
            "zj0DAQcDQgAEV0USlcOJqALSE/bOCdJC/P5/nhk/ntuZ3W9D7gDHOvXvUOoGvLoF\n" +
            "7eLvvJLUG6xUKE/0LAYnt0AatkaqXJl+HKNNMEswDgYDVR0PAQH/BAQDAgeAMAwG\n" +
            "A1UdEwEB/wQCMAAwKwYDVR0jBCQwIoAgGaYQMUmOmzTPqvUdhgbf/xoJZv8PnPjA\n" +
            "7q7obd/C8/MwCgYIKoZIzj0EAwIDRwAwRAIgcsBbAg2dL+mHEZncu5A1T5p5bQZ0\n" +
            "SY3XruDcb1viHxoCIAflfnXFPE7V9kLTZTQF/tOEW3+4QjyrZz6F3ijZOJMR\n" +
            "-----END CERTIFICATE-----" ;
    public static String adminKey = "-----BEGIN PRIVATE KEY-----\n" +
            "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgG6yxPdtqHIXnZSuN\n" +
            "gZbj5TkT0i3Iy38gItYZ3nl1U4ehRANCAARXRRKVw4moAtIT9s4J0kL8/n+eGT+e\n" +
            "25ndb0PuAMc69e9Q6ga8ugXt4u+8ktQbrFQoT/QsBie3QBq2RqpcmX4c\n" +
            "-----END PRIVATE KEY-----";
    public static String pem = "-----BEGIN CERTIFICATE-----\n" +
            "MIICKjCCAdCgAwIBAgIQNrZ7Z8SBFdDjA+IDd966KzAKBggqhkjOPQQDAjBzMQsw\n" +
            "CQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEWMBQGA1UEBxMNU2FuIEZy\n" +
            "YW5jaXNjbzEZMBcGA1UEChMQb3JnMS5leGFtcGxlLmNvbTEcMBoGA1UEAxMTY2Eu\n" +
            "b3JnMS5leGFtcGxlLmNvbTAeFw0xOTA2MjEwOTIyMDBaFw0yOTA2MTgwOTIyMDBa\n" +
            "MGwxCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpDYWxpZm9ybmlhMRYwFAYDVQQHEw1T\n" +
            "YW4gRnJhbmNpc2NvMQ8wDQYDVQQLEwZjbGllbnQxHzAdBgNVBAMMFkFkbWluQG9y\n" +
            "ZzEuZXhhbXBsZS5jb20wWTATBgcqhkjOPQIBBggqhkjOPQMBBwNCAAQHZW/HwQkT\n" +
            "f5TaEISaovg/uXzEE5qBF8KMWpJ+JEmUguray4RLSeYDYh0JRvqCANHbYHJ9YmYI\n" +
            "X517dpYqgXg7o00wSzAOBgNVHQ8BAf8EBAMCB4AwDAYDVR0TAQH/BAIwADArBgNV\n" +
            "HSMEJDAigCAv9S1nRnmgHdldSxOLd76NXVXf8k/t4+wJQ6srk/cSgTAKBggqhkjO\n" +
            "PQQDAgNIADBFAiEAhl4fXRYJyX2bvMDwt7JQYIOGam03cVYVKgMgHsdbYhMCIBuW\n" +
            "brRJY5mBwVGNDNJvPxxka7GYSBsb7/wSEIhBqeli\n" +
            "-----END CERTIFICATE-----";


    public static void main(String[] args) throws Exception {

        // 创建HFClient实例
        HFClient client = HFClient.createNewInstance();
        client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());

        User user= new MyUser("Org0MSP", adminPem, adminKey);

        client.setUserContext(user);


        // 创建通道实例
        Channel channel = client.newChannel("channel01");
        Properties opts = new Properties();

        Peer peer = client.newPeer("peer0","grpc://192.168.235.128:7051", opts);
        channel.addPeer(peer, Channel.PeerOptions.createPeerOptions());

        Orderer orderer = client.newOrderer("orderer0","grpc://192.168.235.128:7050");
        channel.addOrderer(orderer);
        channel.initialize();

        // 查询链码
        QueryByChaincodeRequest request = client.newQueryProposalRequest();
        ChaincodeID cid = ChaincodeID.newBuilder().setName("mywallet").build();
        request.setChaincodeID(cid);
        request.setFcn("value");
        ProposalResponse[] rsp = channel.queryByChaincode(request).toArray(new ProposalResponse[0]);
//        System.out.format("rsp message => %s\n",rsp[0].getProposalResponse().getResponse().getPayload().toStringUtf8());


        // 提交链码交易
        TransactionProposalRequest req2 = client.newTransactionProposalRequest();
        req2.setChaincodeID(cid);
        req2.setFcn("inc");
        req2.setArgs("10");
        Collection<ProposalResponse> rsp2 = channel.sendTransactionProposal(req2);
        BlockEvent.TransactionEvent event = channel.sendTransaction(rsp2).get();
        System.out.format("txid: %s\n", event.getTransactionID());
        System.out.format("valid: %b\n", event.isValid());

    }
}
