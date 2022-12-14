package com.example.exchangeabletoken

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream

class DataBaseService {
    fun addUser(uid: String, name: String, email: String, phone: String, address: String) {
        val database = Firebase.database
        val myRef = database.getReference("users")
        myRef.child(name).child("name").setValue(name.lowercase())
        myRef.child(name).child("uid").setValue(uid)
        myRef.child(name).child("email").setValue(email.lowercase())
        myRef.child(name).child("phone").setValue(phone)
        myRef.child(name).child("address").setValue(address.lowercase())
    }

    fun addTransaction(uid: String, name: String, amount: String, type: String, date: String) {
        val database = Firebase.database
        val myRef = database.getReference("transactions")
        myRef.child(uid).child(name).child("amount").setValue(amount)
        myRef.child(uid).child(name).child("type").setValue(type)
        myRef.child(uid).child(name).child("date").setValue(date)
    }

    companion object {
        fun mockMarketData() {
            FirebaseDatabase.getMarketData().forEach {
                val database = Firebase.database
                val myRef = database.getReference("products")
                myRef.child(it.name).child("id").setValue(it.id)
                myRef.child(it.name).child("name").setValue(it.name.lowercase())
                myRef.child(it.name).child("price").setValue(it.price)
                myRef.child(it.name).child("category").setValue(it.category.lowercase())

                // create randomized mock bitmap
                val bitmap = it.image

                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

                // add image to firebase
                val storage = FirebaseStorageService.getInstance()
                val storageRef = storage.getReference("images/${it.name}.jpg")
                storageRef.putBytes(baos.toByteArray())
                    .addOnSuccessListener {
                        Log.d("FirebaseStorage", "Image uploaded successfully")
                    }
                    .addOnFailureListener {
                        Log.d("FirebaseStorage", "Image upload failed")
                    }
            }
        }


        fun addProduct(name: String, price: Int, image: Bitmap, category: String) {
            Firebase.database.reference.child("products").child("lastId").get()
                .addOnSuccessListener {
                    // generate random number
                    val id = (0..100000).random()
                    Firebase.database.reference.child("products").child(id.toString()).child("id")
                        .setValue(id)
                    Firebase.database.reference.child("products").child(id.toString()).child("name")
                        .setValue(name.lowercase())
                    Firebase.database.reference.child("products").child(id.toString())
                        .child("price").setValue(price)
                    Firebase.database.reference.child("products").child(id.toString())
                        .child("category").setValue(category.lowercase())
                    // Add to firebase storage image
                    Firebase.database.reference.child("images").child(id.toString()).setValue(image)
                }
                .addOnFailureListener {
                    // Handle any errors
                }
        }

        fun getProductsByCategory(category: String): List<DataProduct> {
            val products = mutableListOf<DataProduct>()
            Firebase.database.reference.child("products").get()
                .addOnSuccessListener {
                    it.children.forEach { product ->
                        if (product.child("category").value.toString() == category) {
                            products.add(
                                DataProduct(
                                    product.child("id").value.toString().toInt(),
                                    product.child("name").value.toString(),
                                    product.child("price").value.toString().toInt(),
                                    // TODO: placeholder
                                    Bitmap.createBitmap(
                                        200,
                                        300,
                                        Bitmap.Config.ARGB_8888
                                    ),
                                    product.child("category").value.toString()
                                )
                            )
                        }
                    }
                }
                .addOnFailureListener {
                    // Handle any errors
                }
            return products
        }

        fun addTransaction(id: Int, name: String, price: Int, image: String, category: String) {
            Firebase.database.reference.child("transactions").child("lastId").get()
                .addOnSuccessListener {
                    Firebase.database.reference.child("transactions").child(id.toString())
                        .child("id").setValue(id)
                    Firebase.database.reference.child("transactions").child(id.toString())
                        .child("name").setValue("name")
                    Firebase.database.reference.child("transactions").child(id.toString())
                        .child("price").setValue("price")
                    Firebase.database.reference.child("transactions").child(id.toString())
                        .child("image").setValue("image")
                    Firebase.database.reference.child("transactions").child(id.toString())
                        .child("category").setValue("category")
                }
                .addOnFailureListener {
                    // Handle any errors
                }
        }
    }
}
