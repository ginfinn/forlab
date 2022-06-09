package com.saprikin.store.controller


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.saprikin.store.dto.Dto
import com.saprikin.store.dto.DtoRows
import com.saprikin.store.entity.ChequeHistory
import com.saprikin.store.entity.Product
import com.saprikin.store.entity.ProductHistory
import com.saprikin.store.entity.Store
import com.saprikin.store.repository.ChequeHistoryRepository
import com.saprikin.store.repository.ProductHistoryRepository
import com.saprikin.store.repository.ProductRepository
import com.saprikin.store.repository.StoreRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class MainController(
    private var storeRepository: StoreRepository? = null,
    private var productRepository: ProductRepository? = null,
    private var productHistoryRepository: ProductHistoryRepository? = null,
    private var chequeHistoryRepository: ChequeHistoryRepository? = null
) {

    @PostMapping("/addProduct")
    fun addPositions(@RequestBody products: ArrayList<Product>) {

        for (product in products) {
            val product1: Product? =
                productRepository?.findByProductNameAndStoreId(
                    productName = product.productName,
                    storeId = product.storeId
                )
            if (product1 != null) {
                product1.quantity =   product1.quantity.plus(product.quantity)
                productRepository?.save(product1
                )



            } else {
                productRepository?.save(
                    Product(
                        productName = product.productName,
                        price = product.price,
                        quantity = product.quantity,
                        storeId = product.storeId,
                        category = product.category
                    )
                )

            }
        }


    }

    @PostMapping("/addStore")
    fun addShop(
        @RequestParam name: String?,
        @RequestParam address: String?,
        @RequestParam phoneNumber: String?
    ): ResponseEntity<Any?> {
        return if (!storeRepository?.existsByName(name)!!) {
            ResponseEntity<Any?>(
                java.lang.String.valueOf(
                    storeRepository!!.save(Store(name, address, phoneNumber))
                ), HttpStatus.OK
            )
        } else ResponseEntity.status(400).body("Магазин с таким именем уже есть")
    }


    @GetMapping("/getStoreProduct")
    fun getShopPosition(@RequestParam storeId: Int?): List<Product?>? {

        return productRepository?.findAllByStoreId(storeId)
    }

    @PostMapping("/buy")
    fun buy(@RequestBody dto: Dto): ResponseEntity<*>? {
        val gson: Gson = GsonBuilder().create()
        val productHistories: ArrayList<ProductHistory> = ArrayList<ProductHistory>()
        val set: MutableSet<String> = HashSet()
        var builder: ChequeHistory = ChequeHistory(
            dto.paymentMethod,
            memberId = dto.memberId,
            dto.memberName,
            dto.storeId,
            dto.category,
            productHistories
        )
        if (dto.category.isEmpty()) {
            for (row: DtoRows in dto.productHistory) {
                val product: Product? =
                    productRepository?.findByProductNameAndStoreId(row.productName, dto.storeId)
                if (product != null) {
                    product.category.let { set.add(it) }
                }
            }

            if (set.size > 1) {
                builder.category = "smeshannaia"

            } else {
                builder.category = set.stream().iterator().next()
            }
        } else {
            builder.category = dto.category
        }

        for (row in dto.productHistory) {
            val product: Product? = productRepository?.findByProductNameAndStoreId(row.productName, dto.storeId)
            if (product != null) {
                if (product.quantity < row.quantity) {
                    return ResponseEntity.status(204).build<String>()
                }
            }
            if (product != null) {
                product.quantity = product.quantity.minus(row.quantity)
            }
            if (product != null) {
                productRepository?.save(
                    product
                )

            }
            val builderProductHistory: ProductHistory = ProductHistory(
                product?.productName, product?.price,
                product?.quantity,
            )

            productHistories.add(builderProductHistory)    // add(builderPositionHistory)
        }

        val payloadStr = gson.toJson(
            chequeHistoryRepository?.save(
                ChequeHistory(
                    productHistory = productHistories,
                    paymentMethod = builder.paymentMethod,
                    memberName = builder.memberName,
                    storeId = builder.storeId,
                    category = builder.category,
                    memberId = dto.memberId
                )
            )
        )

        return ResponseEntity(payloadStr, HttpStatus.OK)

    }
}
