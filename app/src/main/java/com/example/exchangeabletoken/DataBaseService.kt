package com.example.exchangeabletoken

import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DataBaseService {
    fun addUser(uid: String, name: String, email: String, phone: String, address: String) {
        val database = Firebase.database
        val myRef = database.getReference("users")
        myRef.child(name).child("name").setValue(name)
        myRef.child(name).child("uid").setValue(uid)
        myRef.child(name).child("email").setValue(email)
        myRef.child(name).child("phone").setValue(phone)
        myRef.child(name).child("address").setValue(address)
    }

    companion object {
        fun createProduct() {
            FirebaseDatabase.getMarketData().forEach {
                Firebase.database.reference.child("products").child(it.id.toString()).setValue(it)
            }
        }

        fun addProduct(name: String, price: Int, image: String, category: String) {
            Firebase.database.reference.child("products").child("lastId").get().addOnSuccessListener {
                // generate random number
                val id = (0..100000).random()
                Firebase.database.reference.child("products").child(id.toString()).child("id").setValue(id)
                Firebase.database.reference.child("products").child(id.toString()).child("name").setValue(name)
                Firebase.database.reference.child("products").child(id.toString()).child("price").setValue(price)
                Firebase.database.reference.child("products").child(id.toString()).child("image").setValue(image)
                Firebase.database.reference.child("products").child(id.toString()).child("category").setValue(category)
            }
                .addOnFailureListener {
                    // Handle any errors
                }
        }

        fun getProductsByCategory(category: String): List<DataProduct> {
            val productsByCategory = mutableListOf<DataProduct>()
            Firebase.database.reference.child("products").get().addOnSuccessListener {
                val products = it.children
                products.forEach {
                    if (it.child("category").value == category) {
                        val product = DataProduct(
                            it.child("id").value.toString().toInt(),
                            it.child("name").value.toString(),
                            it.child("price").value.toString().toInt(),
                            it.child("image").value.toString(),
                            it.child("category").value.toString()
                        )
                        println("product: $product")
                        productsByCategory.add(product)
                    }
                }
            }
                .addOnFailureListener {
                    // Handle any errors
                }
            return productsByCategory
            }
        }
    }
