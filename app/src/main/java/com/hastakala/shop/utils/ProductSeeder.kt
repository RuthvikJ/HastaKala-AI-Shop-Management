package com.hastakala.shop.utils

import com.hastakala.shop.data.model.Product

object ProductSeeder {

    fun getProducts(): List<Product> = listOf(
        Product(
            id = 1,
            name = "Terracotta Pot Set",
            category = "Pottery",
            stock = 25,
            price = 1200.0,
            artisanName = "Ramesh Kumar",
            description = "Handcrafted terracotta pot set with traditional Madhubani motifs"
        ),
        Product(
            id = 2,
            name = "Handwoven Pashmina Shawl",
            category = "Textiles",
            stock = 8,
            price = 4500.0,
            artisanName = "Fatima Begum",
            description = "Luxurious pashmina shawl with intricate hand embroidery"
        ),
        Product(
            id = 3,
            name = "Wooden Elephant Figurine",
            category = "Woodcraft",
            stock = 40,
            price = 850.0,
            artisanName = "Suresh Vishwakarma",
            description = "Sandalwood carved elephant with fine detailing"
        ),
        Product(
            id = 4,
            name = "Brass Diya Set (5 pcs)",
            category = "Metalwork",
            stock = 60,
            price = 650.0,
            artisanName = "Mohan Thakur",
            description = "Traditional brass oil lamp set for festivals and puja"
        ),
        Product(
            id = 5,
            name = "Bamboo Fruit Basket",
            category = "Bamboo Craft",
            stock = 35,
            price = 450.0,
            artisanName = "Lakshmi Devi",
            description = "Eco-friendly hand-woven bamboo basket with sturdy build"
        ),
        Product(
            id = 6,
            name = "Madhubani Painting (A3)",
            category = "Paintings",
            stock = 12,
            price = 2800.0,
            artisanName = "Sita Devi",
            description = "Original Madhubani painting depicting village harvest scene"
        ),
        Product(
            id = 7,
            name = "Clay Diyas (Pack of 12)",
            category = "Pottery",
            stock = 100,
            price = 180.0,
            artisanName = "Ramesh Kumar",
            description = "Hand-shaped festive clay diyas with colorful glazing"
        ),
        Product(
            id = 8,
            name = "Jute Tote Bag",
            category = "Textiles",
            stock = 45,
            price = 350.0,
            artisanName = "Anjali Mahato",
            description = "Durable hand-stitched jute bag with block print design"
        ),
        Product(
            id = 9,
            name = "Stone Mortar & Pestle",
            category = "Stonecraft",
            stock = 3,
            price = 1100.0,
            artisanName = "Bhola Nath",
            description = "Heavy-duty carved stone sil-batta for traditional grinding"
        ),
        Product(
            id = 10,
            name = "Lacquer Bangle Set",
            category = "Jewelry",
            stock = 70,
            price = 220.0,
            artisanName = "Meena Kumari",
            description = "Colorful lac bangles with mirror work and glitter finish"
        ),
        Product(
            id = 11,
            name = "Chikankari Kurta (Men)",
            category = "Textiles",
            stock = 15,
            price = 1800.0,
            artisanName = "Rehana Khatoon",
            description = "Premium hand-embroidered Lucknowi chikankari cotton kurta"
        ),
        Product(
            id = 12,
            name = "Copper Water Bottle",
            category = "Metalwork",
            stock = 20,
            price = 750.0,
            artisanName = "Mohan Thakur",
            description = "Hammered pure copper bottle with Ayurvedic health benefits"
        ),
        Product(
            id = 13,
            name = "Coconut Shell Lamp",
            category = "Eco Decor",
            stock = 18,
            price = 550.0,
            artisanName = "Priya Nair",
            description = "Hand-carved coconut shell table lamp with warm LED glow"
        ),
        Product(
            id = 14,
            name = "Handmade Silver Earrings",
            category = "Jewelry",
            stock = 30,
            price = 1350.0,
            artisanName = "Kavita Joshi",
            description = "Oxidized silver jhumkas with filigree work and pearl drops"
        ),
        Product(
            id = 15,
            name = "Clay Pottery Vase",
            category = "Pottery",
            stock = 22,
            price = 780.0,
            artisanName = "Ramesh Kumar",
            description = "Wheel-thrown pottery vase with hand-painted floral patterns"
        ),
        Product(
            id = 16,
            name = "Bamboo Wind Chime",
            category = "Bamboo Craft",
            stock = 50,
            price = 320.0,
            artisanName = "Lakshmi Devi",
            description = "Natural bamboo wind chime with soothing melodic tones"
        ),
        Product(
            id = 17,
            name = "Jute Macramé Wall Art",
            category = "Eco Decor",
            stock = 4,
            price = 1500.0,
            artisanName = "Anjali Mahato",
            description = "Handwoven jute macramé wall hanging with boho pattern"
        ),
        Product(
            id = 18,
            name = "Warli Art Frame (A4)",
            category = "Paintings",
            stock = 16,
            price = 1200.0,
            artisanName = "Sita Devi",
            description = "Traditional Warli tribal painting on handmade paper"
        )
    )
}
