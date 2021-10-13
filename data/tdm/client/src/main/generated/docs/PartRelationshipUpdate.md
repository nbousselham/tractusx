
# PartRelationshipUpdate

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**effectTime** | [**OffsetDateTime**](OffsetDateTime.md) | Instant at which the update was applied | 
**relationship** | [**PartRelationship**](PartRelationship.md) |  | 
**remove** | **Boolean** | &lt;ul&gt;   &lt;li&gt;TRUE if the child is not part of the parent (used to update data, e.g. a relationship was wrongly submitted, or a part is removed from a car during maintenance)&lt;/li&gt;   &lt;li&gt;FALSE otherwise (“normal case” - a part is added into a parent part).&lt;/li&gt;&lt;/ul&gt; |  [optional]
**stage** | [**StageEnum**](#StageEnum) | Stage defining whether changes apply to the AS_BUILT or AS_MAINTAINED BOM views. | 



<a name="StageEnum"></a>
## Enum: StageEnum
Name | Value
---- | -----
BUILD | &quot;BUILD&quot;
MAINTENANCE | &quot;MAINTENANCE&quot;



