var payloadProto = "package kuradatatypes;option java_package= \"org.eclipse.kura.core.message.protobuf\";option java_outer_classname = \"KuraPayloadProto\";message KuraPayload {message KuraMetric {enum ValueType{DOUBLE = 0;FLOAT = 1;INT64 = 2;INT32 = 3;BOOL = 4;STRING = 5;BYTES = 6;}required string name = 1;required ValueType type = 2;optional double double_value = 3;optional float float_value = 4;optional int64 long_value = 5;optional int32 int_value = 6;optional bool bool_value = 7;optional string string_value = 8;optional bytes bytes_value = 9;}message KuraPosition{required double latitude=1;required double longitude=2;optional double altitude=3;optional double precision=4;optional double heading=5;optional double speed = 6;optional int64 timestamp=7;optional int32 satellites=8;optional int32 status=9;}optional int64 timestamp = 1;optional KuraPosition position  = 2;extensions 3 to 4999;repeated KuraMetric metric=5000;optional bytes body= 5001;}"
var payloadType = protobuf.parse(payloadProto).root.lookupType('kuradatatypes.KuraPayload')

var KuraPayload = {
  decode: function(rawData) {
      return payloadType.decode(rawData)
  },
  encode: function(payload) {
    var err = payloadType.verify(payload)
    if (err) {
      throw err
    }
    var message = payloadType.fromObject(payload)
    return payloadType.encode(message).finish()
  }
}
