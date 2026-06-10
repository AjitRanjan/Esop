package faceembedding

import com.example.esop.district.DistrictItem
import com.example.esop.state.StateItem

data class SubmitResponse(

    val responseCode: Int,

    val responseDesc: String,

    val wrappedLista: List<WrappedListaItem>
)
