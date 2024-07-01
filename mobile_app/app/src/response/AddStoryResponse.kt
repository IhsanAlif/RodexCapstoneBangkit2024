package com.alice.rodexapp.response

import com.google.gson.annotations.SerializedName

class AddStoryResponse (@field:SerializedName("error")
val error: Boolean,

@field:SerializedName("message")
val message: String
)