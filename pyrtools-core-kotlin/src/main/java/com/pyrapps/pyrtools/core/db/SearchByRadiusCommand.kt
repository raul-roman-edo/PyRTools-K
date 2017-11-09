package com.pyrapps.pyrtools.core.db

class SearchByRadiusCommand(private val tableName: String, private val latitudeColName: String,
                            private val longitudeColName: String) {
    companion object {
        private val EARTH_RAD_KM = 6371f
        private val TORAD = 3.14159265359f / 180f
        // Taylor sine (order 2)
        private val TAYLOR_SIN =
                "(%s) - (%s) * (%s) * (%s) / 6 + (%s) * (%s) * (%s) * (%s) * (%s)/ 120"
        // Taylor cosine (order 2)
        private val TAYLOR_COS = "1 - (%s) * (%s) / 2 + (%s) * (%s) * (%s) * (%s) / 24"
        private val QUERY_TEMPLATE = "SELECT *, Distance FROM (SELECT *, %s AS Distance FROM %s) " +
                "t WHERE Distance<%s ORDER BY Distance;"
        // Haversine formula
        private val HAVERSINE_TEMPLATE = "(%s) * (%s) + (%s) * (%s) * (%s) * (%s)"
        private val COORDINATE_DIFF_TEMPLATE = "%s * (%s - %s)/2"
        private val MULTIPLICATION = "%s * %s"
    }

    fun buildQuery(latitude: Float, longitude: Float, radiusInKm: Float)
            = buildHaversineDistanceQuery(latitude, longitude, radiusInKm)

    private fun buildHaversineDistanceQuery(latitude: Float,
                                            longitude: Float,
                                            radiusInKm: Float) = {
        val r = Math.pow(Math.sin(radiusInKm.toDouble() / (2 * EARTH_RAD_KM)), 2.0).toFloat()

        val dlat = String.format(COORDINATE_DIFF_TEMPLATE, TORAD, latitudeColName, latitude)
        val dlon = String.format(COORDINATE_DIFF_TEMPLATE, TORAD, longitudeColName, longitude)
        val coslat1 = Math.cos((latitude * TORAD).toDouble()).toFloat()
        val lat2 = String.format(MULTIPLICATION, TORAD, latitudeColName)

        val a = String.format(HAVERSINE_TEMPLATE,
                String.format(TAYLOR_SIN, dlat, dlat, dlat, dlat, dlat, dlat, dlat, dlat, dlat),
                String.format(TAYLOR_SIN, dlat, dlat, dlat, dlat, dlat, dlat, dlat, dlat, dlat),
                coslat1, String.format(TAYLOR_COS, lat2, lat2, lat2, lat2, lat2, lat2),
                String.format(TAYLOR_SIN, dlon, dlon, dlon, dlon, dlon, dlon, dlon, dlon, dlon),
                String.format(TAYLOR_SIN, dlon, dlon, dlon, dlon, dlon, dlon, dlon, dlon, dlon))
        String.format(QUERY_TEMPLATE, a, tableName, r)
    }
}