package com.mfitrahrmd.githubuser.repositories

open class Pagination(var first: Int?, var last: Int?, var previous: Int?, var next: Int?)
class WithPagination<T>(val data: T?, first: Int?, last: Int?, previous: Int?, next: Int?) :
    Pagination(first, last, previous, next)