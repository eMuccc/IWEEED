package com.yunna.cc.ingore.bean

import org.litepal.crud.LitePalSupport

data class LifeAdviceBean(
    val category: String,
    val date: String,
    val level: String,
    val name: String,
    val text: String,
    val type: String,
) : LitePalSupport()
