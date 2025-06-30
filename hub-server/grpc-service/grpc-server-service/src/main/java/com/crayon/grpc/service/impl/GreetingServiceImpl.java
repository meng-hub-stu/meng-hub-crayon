//package com.crayon.grpc.service.impl;
//import proto.GreetProto;
//import io.grpc.stub.StreamObserver;
//import net.devh.boot.grpc.server.service.GrpcService;
//
///**
// * @author Mengdl
// * @date 2025/06/25
// */
//@GrpcService
//public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {
//
//    @Override
//    public void sayHello(GreetProto.HelloRequest request, StreamObserver<GreetProto.HelloReply> responseObserver) {
//        String message = "Hello, " + request.getName();
//        GreetProto.HelloReply reply = GreetProto.HelloReply.newBuilder().setMessage(message).build();
//        responseObserver.onNext(reply);
//        responseObserver.onCompleted();
//    }
//
//}
