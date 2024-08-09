package com.tourch.exception

import com.tourch.annotation.Constants
import java.io.IOException

/**
 * This is No internet exception class which directly implemented in service generator
 */

class NoInternetException : IOException() {

    override val message: String
        get() = Constants.NO_INTERNET


}