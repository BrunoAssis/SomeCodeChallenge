package com.brunoassis.corachallenge.infrastructure.dao

interface Dao<T> {
    fun insert(toBeInserted: T): T
    // fun update(toBeUpdated: T) : T - Not used in this project. Added here as an idea.
    // fun delete(id: Long) : Boolean - Not used in this project. Added here as an idea.
    fun findById(id: Long): T
}
