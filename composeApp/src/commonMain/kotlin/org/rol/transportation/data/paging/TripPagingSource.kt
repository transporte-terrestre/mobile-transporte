package org.rol.transportation.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.rol.transportation.data.remote.api.TripApi
import org.rol.transportation.data.remote.dto.trip.TripDto
import org.rol.transportation.domain.model.Trip

class TripPagingSource (
    private val tripApi: TripApi,
    private val toDomain: (TripDto) -> Trip
) : PagingSource<Int, Trip>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Trip> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize

            val response = tripApi.getTrips(page = page, limit = pageSize)

            val trips = response.data.map { toDomain(it) }
            val totalPages = response.meta.totalPages

            LoadResult.Page(
                data = trips,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page >= totalPages) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Trip>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}