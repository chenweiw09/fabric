package com.my.chen.fabric.mc.user;

import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.util.Collection;

/**
 * @author chenwei
 * @version 1.0
 * @date 2019/6/14
 * @description
 */
public class App {


    public static void main(String[] args) throws Exception {
        String keyFile = "";
        String certFile = "";
        MyUser user = new MyUser();
        user.setName("admin");
        user.setEnrollment(user.loadFromPemFile(keyFile,certFile));
        user.setMspId("SampleOrg");

        // 创建HFClient实例
        HFClient client = HFClient.createNewInstance();
        client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        client.setUserContext(user);


        // 创建通道实例
        Channel channel = client.newChannel("chi");
        Peer peer = client.newPeer("peer1","grpc://127.0.0.1:7051");
        channel.addPeer(peer);

        Orderer orderer = client.newOrderer("orderer1","grpc://127.0.0.1:7050");
        channel.addOrderer(orderer);
        channel.initialize();

        // 查询链码
        QueryByChaincodeRequest request = client.newQueryProposalRequest();
        ChaincodeID cid = ChaincodeID.newBuilder().setName("mywallet").build();
        request.setChaincodeID(cid);
        request.setFcn("value");
        ProposalResponse[] rsp = channel.queryByChaincode(request).toArray(new ProposalResponse[0]);
        System.out.format("rsp message => %s\n",rsp[0].getProposalResponse().getResponse().getPayload().toStringUtf8());


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
