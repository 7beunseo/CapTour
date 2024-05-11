package com.example.captour

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name="response") // 이러한 xml response를 만난다면
data class XmlResponse (
    @Element // 내 안에 또 다른 elememt
    val body: myXmlBody
)

@Xml(name="body") // body를 만난다면
data class myXmlBody(
    @Element // 또 다른 element 가 들어간다면
    val items: myXmlItems
)

@Xml(name="items")
data class myXmlItems (
    @Element
    val item: MutableList<myXmlItem>
)

@Xml(name="item")
data class myXmlItem(
    @PropertyElement
    val galTitle: String?,

    @PropertyElement
    val galWebImageUrl: String?,

    @PropertyElement
    val galCreatedtime: String?,

    @PropertyElement
    val galPhotographyLocation: String?,

    @PropertyElement
    val galPhotographer: String?,

    @PropertyElement
    val galSearchKeyword: String?,

    ) {
    constructor(): this(null, null, null, null, null, null,)
}