package fr.corenting.edcompanion.models

import fr.corenting.edcompanion.models.apis.EDSM.EDSMSystem

data class SystemFinderResult(val name: String, val id:Long, val isPermitRequired:Boolean,
                              val allegiance:String?, val security:String?, val government:String?,
                              val economy:String?) {
    companion object {
        fun fromEDSMSystem(res: EDSMSystem): SystemFinderResult {
            var allegiance: String? = null
            var security: String? = null
            var government: String? = null
            var economy: String? = null
            if (res.Information != null) {
                allegiance = res.Information.Allegiance
                government = res.Information.Government
                security = res.Information.Security
                economy = res.Information.Economy
            }
            return SystemFinderResult(res.Name, res.Id, res.PermitRequired, allegiance,
                    security, government, economy)
        }
    }
}

