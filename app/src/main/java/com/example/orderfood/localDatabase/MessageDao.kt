package com.example.orderfood.localDatabase

import androidx.room.*
import com.example.orderfood.model.CartItems
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: CartItems)

    @Query("SELECT * FROM cart_items ORDER BY id DESC")
    suspend fun getAllMessages(): List<CartItems>

    @Query("SELECT * FROM cart_items ORDER BY id DESC")
    fun getAllMessagesFlow(): Flow<List<CartItems>>

    @Query("UPDATE cart_items SET quantity = :newQuantity WHERE id = :itemId")
    fun updateQuantity(itemId: Int, newQuantity: Int)

    @Query("DELETE FROM cart_items WHERE id = :itemId")
    suspend fun deleteById(itemId: Int)

    @Query("SELECT * FROM cart_items WHERE id = :itemId")
    suspend fun searchById(itemId: Int): CartItems

}
