package com.dag.todoappjetpack.util

enum class Action {
    ADD,
    DELETE,
    UPDATE,
    DELETE_ALL,
    UNDO,
    NO_ACTION
}

fun String?.toAction(): Action {
    return when{
        this == "ADD" -> {
            Action.ADD
        }
        this == "DELETE" -> {
            Action.DELETE
        }
        this == "UPDATE" -> {
            Action.UPDATE
        }
        this == "DELETE_ALL" -> {
            Action.DELETE_ALL
        }
        this == "UNDO" -> {
            Action.UNDO
        }
        this == "NO_ACTION" -> {
            Action.NO_ACTION
        }
        else ->{
            Action.NO_ACTION
        }
    }
}