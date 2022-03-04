package com.dag.todoappjetpack.util

import java.lang.Exception

enum class Action {
    ADD,
    DELETE,
    UPDATE,
    DELETE_ALL,
    UNDO,
    NO_ACTION
}

fun String?.toAction(): Action {
    return try{
        if (this.isNullOrEmpty()) Action.NO_ACTION else Action.valueOf(this)
    }catch (e:Exception){
        Action.NO_ACTION
    }
}