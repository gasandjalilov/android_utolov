package uz.rdu.ucell_utolov.helpers


object Constants {
    val BASE_URL = "http://188.113.225.10:8060"
    val AUTH_URL = BASE_URL +"/v1/auth/"
    val PROFILE_URL = BASE_URL+"/v1/profile/"
    val BALANCE_URL = BASE_URL + "/v1/balance/"
    val PIN_URL = BASE_URL + "/v1/pin/"
    val TRANSACTION_HOSTORY_URL = BASE_URL + "/v1/transactionhistory/"
    val TRANSACTION_PERFORM_URL = BASE_URL + "/v1/transactionperform/"
    val MERCHANT_URL = BASE_URL + "/v1/merchants-service/"
    val P2P_URL = BASE_URL + "/v1/p2p-service/"
    val UCELL_PROFILE = BASE_URL + "/v1/ucellprofile-service/"
    val WEBSOCKET = BASE_URL + "/v1/websocket-service/"
    val ARTICLE_URL = BASE_URL + "/v1/article-service/"

    val WEBSOCKETWS = "ws://188.113.225.10:8060/"

    val LDAP_HOSTNAME= "10.7.8.17" //"10.7.8.17" "10.2.18.146"
    val LDAP_PORT=3268
}