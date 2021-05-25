package com.example.fpauthenticationstep.ModelsForNearby

class Result {


    var geometry: Geometry?=null
    var icon: String?=null
    var id: String?=null
    var name: String?=null
    var opening_hours: OpeningHours?=null
    var photos: Array<Photo>?=null
    var place_id: String?=null
    var address_components: Array<AddressComponent>?=null
    var adr_address: String?=null
    var rating: Double=0.0
    var reference: String?=null
    var scope: String?=null
    var formatted_address: String?=null
    var formatted_phone_number: String?=null
    var international_phone_number: String?=null
    var reviews: Array<Review>?=null
    var url: String?=null
    var utc_offset: Int=0
    var website: String?=null
    var types: Array<String>?=null
    var vicinity: String?=null
    var price_level: Int = 0
    var user_ratings_total: Int = 0
    var plus_code: PlusCode?=null
}