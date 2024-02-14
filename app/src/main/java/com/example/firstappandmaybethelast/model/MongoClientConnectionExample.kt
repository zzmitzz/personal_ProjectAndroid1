//package com.example.firstappandmaybethelast.model
//import android.util.Log
//import com.mongodb.ConnectionString
//import com.mongodb.MongoClientSettings
//import com.mongodb.ServerApi
//import com.mongodb.ServerApiVersion
//import com.mongodb.kotlin.client.coroutine.MongoClient
//
// SINCE THE DRIVER NOT STABLE, CALL API INSTEAD.
//object MongoClientConnectionExample {
//
//
//        // Replace the placeholders with your credentials and hostname
//        private const val connectionString = "mongodb+srv://xorada8242:MongoDB.com123%40@cluster0.155yc0l.mongodb.net/?retryWrites=true&w=majority"
//        private val serverApi = ServerApi.builder()
//            .version(ServerApiVersion.V1)
//            .build()
//        private val mongoClientSettings = MongoClientSettings.builder()
//            .applyConnectionString(ConnectionString(connectionString))
//            .serverApi(serverApi)
//            .build()
//        // Create a new client and connect to the server
//        init {
//            MongoClient.create(mongoClientSettings).also {
//                Log.d("MongoDB","Successful")
////                val database = mongoClient.getDatabase("admin")
////                CoroutineScope(Dispatchers.IO).launch {
////                    database.runCommand(Document("ping", 1))
////                }
//            }
//        }
//
//}