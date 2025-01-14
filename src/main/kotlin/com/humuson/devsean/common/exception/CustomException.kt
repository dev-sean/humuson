package com.humuson.devsean.common.exception

class ExternalSystemException(message: String? = null, throwable: Throwable? = null) :
    RuntimeException(message, throwable)

class DataValidationException(message: String) : RuntimeException(message)
