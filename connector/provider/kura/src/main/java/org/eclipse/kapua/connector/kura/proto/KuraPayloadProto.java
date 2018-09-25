/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.connector.kura.proto;

public final class KuraPayloadProto {

    private KuraPayloadProto() {
    }

    public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    }

    public interface KuraPayloadOrBuilder extends
            // @@protoc_insertion_point(interface_extends:kuradatatypes.KuraPayload)
            com.google.protobuf.GeneratedMessage.ExtendableMessageOrBuilder<KuraPayload> {

        /**
         * <code>optional int64 timestamp = 1;</code>
         */
        boolean hasTimestamp();

        /**
         * <code>optional int64 timestamp = 1;</code>
         */
        long getTimestamp();

        /**
         * <code>optional .kuradatatypes.KuraPayload.KuraPosition position = 2;</code>
         */
        boolean hasPosition();

        /**
         * <code>optional .kuradatatypes.KuraPayload.KuraPosition position = 2;</code>
         */
        org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition getPosition();

        /**
         * <code>optional .kuradatatypes.KuraPayload.KuraPosition position = 2;</code>
         */
        org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPositionOrBuilder getPositionOrBuilder();

        /**
         * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
         *
         * <pre>
         * can be zero, so optional
         * </pre>
         */
        java.util.List<org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric> getMetricList();

        /**
         * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
         *
         * <pre>
         * can be zero, so optional
         * </pre>
         */
        org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric getMetric(int index);

        /**
         * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
         *
         * <pre>
         * can be zero, so optional
         * </pre>
         */
        int getMetricCount();

        /**
         * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
         *
         * <pre>
         * can be zero, so optional
         * </pre>
         */
        java.util.List<? extends org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetricOrBuilder> getMetricOrBuilderList();

        /**
         * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
         *
         * <pre>
         * can be zero, so optional
         * </pre>
         */
        org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetricOrBuilder getMetricOrBuilder(
                int index);

        /**
         * <code>optional bytes body = 5001;</code>
         */
        boolean hasBody();

        /**
         * <code>optional bytes body = 5001;</code>
         */
        com.google.protobuf.ByteString getBody();
    }

    /**
     * Protobuf type {@code kuradatatypes.KuraPayload}
     */
    public static final class KuraPayload extends com.google.protobuf.GeneratedMessage.ExtendableMessage<KuraPayload>
            implements
            // @@protoc_insertion_point(message_implements:kuradatatypes.KuraPayload)
            KuraPayloadOrBuilder {

        // Use KuraPayload.newBuilder() to construct.
        private KuraPayload(
                com.google.protobuf.GeneratedMessage.ExtendableBuilder<org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload, ?> builder) {
            super(builder);
            this.unknownFields = builder.getUnknownFields();
        }

        private KuraPayload(boolean noInit) {
            this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance();
        }

        private static final KuraPayload DEFAULT_INSTANCE;

        public static KuraPayload getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        @Override
        public KuraPayload getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }

        private final com.google.protobuf.UnknownFieldSet unknownFields;

        @java.lang.Override
        public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private KuraPayload(com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                        throws com.google.protobuf.InvalidProtocolBufferException {
            initFields();
            int mutableBitField0 = 0;
            com.google.protobuf.UnknownFieldSet.Builder unknownFields = com.google.protobuf.UnknownFieldSet
                    .newBuilder();
            try {
                boolean done = false;
                while (!done) {
                    int tag = input.readTag();
                    switch (tag) {
                    case 0:
                        done = true;
                        break;
                    default: {
                        if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                            done = true;
                        }
                        break;
                    }
                    case 8: {
                        this.bitField0 |= 0x00000001;
                        this.timestamp = input.readInt64();
                        break;
                    }
                    case 18: {
                        org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition.Builder subBuilder = null;
                        if ((this.bitField0 & 0x00000002) == 0x00000002) {
                            subBuilder = this.position.toBuilder();
                        }
                        this.position = input.readMessage(
                                org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition.PARSER,
                                extensionRegistry);
                        if (subBuilder != null) {
                            subBuilder.mergeFrom(this.position);
                            this.position = subBuilder.buildPartial();
                        }
                        this.bitField0 |= 0x00000002;
                        break;
                    }
                    case 40002: {
                        if (!((mutableBitField0 & 0x00000004) == 0x00000004)) {
                            this.metric = new java.util.ArrayList<org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric>();
                            mutableBitField0 |= 0x00000004;
                        }
                        this.metric.add(input.readMessage(
                                org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.PARSER,
                                extensionRegistry));
                        break;
                    }
                    case 40010: {
                        this.bitField0 |= 0x00000004;
                        this.body = input.readBytes();
                        break;
                    }
                    }
                }
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                throw e.setUnfinishedMessage(this);
            } catch (java.io.IOException e) {
                throw new com.google.protobuf.InvalidProtocolBufferException(e.getMessage()).setUnfinishedMessage(this);
            } finally {
                if ((mutableBitField0 & 0x00000004) == 0x00000004) {
                    this.metric = java.util.Collections.unmodifiableList(this.metric);
                }
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
            return org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.INTERNAL_STATIC_KARADATATYPES_KURAPAYLOAD_DESCRIPTOR;
        }

        @Override
        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.internalStaticKuradatatypesKuraPayloadFieldAccessorTable
                    .ensureFieldAccessorsInitialized(
                            org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.class,
                            org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.Builder.class);
        }

        public static final com.google.protobuf.Parser<KuraPayload> PARSER = new com.google.protobuf.AbstractParser<KuraPayload>() {

            @Override
            public KuraPayload parsePartialFrom(com.google.protobuf.CodedInputStream input,
                    com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                            throws com.google.protobuf.InvalidProtocolBufferException {
                return new KuraPayload(input, extensionRegistry);
            }
        };

        @java.lang.Override
        public com.google.protobuf.Parser<KuraPayload> getParserForType() {
            return PARSER;
        }

        public interface KuraMetricOrBuilder extends
                // @@protoc_insertion_point(interface_extends:kuradatatypes.KuraPayload.KuraMetric)
                com.google.protobuf.MessageOrBuilder {

            /**
             * <code>required string name = 1;</code>
             */
            boolean hasName();

            /**
             * <code>required string name = 1;</code>
             */
            java.lang.String getName();

            /**
             * <code>required string name = 1;</code>
             */
            com.google.protobuf.ByteString getNameBytes();

            /**
             * <code>required .kuradatatypes.KuraPayload.KuraMetric.ValueType type = 2;</code>
             */
            boolean hasType();

            /**
             * <code>required .kuradatatypes.KuraPayload.KuraMetric.ValueType type = 2;</code>
             */
            org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.ValueType getType();

            /**
             * <code>optional double double_value = 3;</code>
             */
            boolean hasDoubleValue();

            /**
             * <code>optional double double_value = 3;</code>
             */
            double getDoubleValue();

            /**
             * <code>optional float float_value = 4;</code>
             */
            boolean hasFloatValue();

            /**
             * <code>optional float float_value = 4;</code>
             */
            float getFloatValue();

            /**
             * <code>optional int64 long_value = 5;</code>
             */
            boolean hasLongValue();

            /**
             * <code>optional int64 long_value = 5;</code>
             */
            long getLongValue();

            /**
             * <code>optional int32 int_value = 6;</code>
             */
            boolean hasIntValue();

            /**
             * <code>optional int32 int_value = 6;</code>
             */
            int getIntValue();

            /**
             * <code>optional bool bool_value = 7;</code>
             */
            boolean hasBoolValue();

            /**
             * <code>optional bool bool_value = 7;</code>
             */
            boolean getBoolValue();

            /**
             * <code>optional string string_value = 8;</code>
             */
            boolean hasStringValue();

            /**
             * <code>optional string string_value = 8;</code>
             */
            java.lang.String getStringValue();

            /**
             * <code>optional string string_value = 8;</code>
             */
            com.google.protobuf.ByteString getStringValueBytes();

            /**
             * <code>optional bytes bytes_value = 9;</code>
             */
            boolean hasBytesValue();

            /**
             * <code>optional bytes bytes_value = 9;</code>
             */
            com.google.protobuf.ByteString getBytesValue();
        }

        /**
         * Protobuf type {@code kuradatatypes.KuraPayload.KuraMetric}
         */
        public static final class KuraMetric extends com.google.protobuf.GeneratedMessage implements
                // @@protoc_insertion_point(message_implements:kuradatatypes.KuraPayload.KuraMetric)
                KuraMetricOrBuilder {

            // Use KuraMetric.newBuilder() to construct.
            private KuraMetric(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
                super(builder);
                this.unknownFields = builder.getUnknownFields();
            }

            private KuraMetric(boolean noInit) {
                this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance();
            }

            private static final KuraMetric DEFAULT_INSTANCE;

            public static KuraMetric getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            @Override
            public KuraMetric getDefaultInstanceForType() {
                return DEFAULT_INSTANCE;
            }

            private final com.google.protobuf.UnknownFieldSet unknownFields;

            @java.lang.Override
            public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
                return this.unknownFields;
            }

            private KuraMetric(com.google.protobuf.CodedInputStream input,
                    com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                            throws com.google.protobuf.InvalidProtocolBufferException {
                initFields();
                com.google.protobuf.UnknownFieldSet.Builder unknownFields = com.google.protobuf.UnknownFieldSet
                        .newBuilder();
                try {
                    boolean done = false;
                    while (!done) {
                        int tag = input.readTag();
                        switch (tag) {
                        case 0:
                            done = true;
                            break;
                        default: {
                            if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                done = true;
                            }
                            break;
                        }
                        case 10: {
                            com.google.protobuf.ByteString bs = input.readBytes();
                            this.bitField0 |= 0x00000001;
                            this.name = bs;
                            break;
                        }
                        case 16: {
                            int rawValue = input.readEnum();
                            org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.ValueType value = org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.ValueType
                                    .valueOf(rawValue);
                            if (value == null) {
                                unknownFields.mergeVarintField(2, rawValue);
                            } else {
                                this.bitField0 |= 0x00000002;
                                this.typeI = value;
                            }
                            break;
                        }
                        case 25: {
                            this.bitField0 |= 0x00000004;
                            this.doubleValue = input.readDouble();
                            break;
                        }
                        case 37: {
                            this.bitField0 |= 0x00000008;
                            this.floatValue = input.readFloat();
                            break;
                        }
                        case 40: {
                            this.bitField0 |= 0x00000010;
                            this.longValue = input.readInt64();
                            break;
                        }
                        case 48: {
                            this.bitField0 |= 0x00000020;
                            this.intValue = input.readInt32();
                            break;
                        }
                        case 56: {
                            this.bitField0 |= 0x00000040;
                            this.boolValue = input.readBool();
                            break;
                        }
                        case 66: {
                            com.google.protobuf.ByteString bs = input.readBytes();
                            this.bitField0 |= 0x00000080;
                            this.stringValue = bs;
                            break;
                        }
                        case 74: {
                            this.bitField0 |= 0x00000100;
                            this.bytesValue = input.readBytes();
                            break;
                        }
                        }
                    }
                } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                    throw e.setUnfinishedMessage(this);
                } catch (java.io.IOException e) {
                    throw new com.google.protobuf.InvalidProtocolBufferException(e.getMessage())
                            .setUnfinishedMessage(this);
                } finally {
                    this.unknownFields = unknownFields.build();
                    makeExtensionsImmutable();
                }
            }

            public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
                return org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.INTERNAL_STATIC_KARADATATYPES_KURAPAYLOAD_KURAMETRIC_DESCRIPTOR;
            }

            @Override
            protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.internalStaticKuradatatypesKuraPayloadKuraMetricFieldAccessorTable
                        .ensureFieldAccessorsInitialized(
                                org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.class,
                                org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.Builder.class);
            }

            public static final com.google.protobuf.Parser<KuraMetric> PARSER = new com.google.protobuf.AbstractParser<KuraMetric>() {

                @Override
                public KuraMetric parsePartialFrom(com.google.protobuf.CodedInputStream input,
                        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                                throws com.google.protobuf.InvalidProtocolBufferException {
                    return new KuraMetric(input, extensionRegistry);
                }
            };

            @java.lang.Override
            public com.google.protobuf.Parser<KuraMetric> getParserForType() {
                return PARSER;
            }

            /**
             * Protobuf enum {@code kuradatatypes.KuraPayload.KuraMetric.ValueType}
             */
            public enum ValueType implements com.google.protobuf.ProtocolMessageEnum {
                /**
                 * <code>DOUBLE = 0;</code>
                 */
                DOUBLE(0, 0), /**
                               * <code>FLOAT = 1;</code>
                               */
                FLOAT(1, 1), /**
                              * <code>INT64 = 2;</code>
                              */
                INT64(2, 2), /**
                              * <code>INT32 = 3;</code>
                              */
                INT32(3, 3), /**
                              * <code>BOOL = 4;</code>
                              */
                BOOL(4, 4), /**
                             * <code>STRING = 5;</code>
                             */
                STRING(5, 5), /**
                               * <code>BYTES = 6;</code>
                               */
                BYTES(6, 6),;

                /**
                 * <code>DOUBLE = 0;</code>
                 */
                public static final int DOUBLE_VALUE = 0;
                /**
                 * <code>FLOAT = 1;</code>
                 */
                public static final int FLOAT_VALUE = 1;
                /**
                 * <code>INT64 = 2;</code>
                 */
                public static final int INT64_VALUE = 2;
                /**
                 * <code>INT32 = 3;</code>
                 */
                public static final int INT32_VALUE = 3;
                /**
                 * <code>BOOL = 4;</code>
                 */
                public static final int BOOL_VALUE = 4;
                /**
                 * <code>STRING = 5;</code>
                 */
                public static final int STRING_VALUE = 5;
                /**
                 * <code>BYTES = 6;</code>
                 */
                public static final int BYTES_VALUE = 6;

                @Override
                public final int getNumber() {
                    return this.value;
                }

                public static ValueType valueOf(int value) {
                    switch (value) {
                    case 0:
                        return DOUBLE;
                    case 1:
                        return FLOAT;
                    case 2:
                        return INT64;
                    case 3:
                        return INT32;
                    case 4:
                        return BOOL;
                    case 5:
                        return STRING;
                    case 6:
                        return BYTES;
                    default:
                        return null;
                    }
                }

                public static com.google.protobuf.Internal.EnumLiteMap<ValueType> internalGetValueMap() {
                    return internalValueMap;
                }

                private static com.google.protobuf.Internal.EnumLiteMap<ValueType> internalValueMap = new com.google.protobuf.Internal.EnumLiteMap<ValueType>() {

                    @Override
                    public ValueType findValueByNumber(int number) {
                        return ValueType.valueOf(number);
                    }
                };

                @Override
                public final com.google.protobuf.Descriptors.EnumValueDescriptor getValueDescriptor() {
                    return getDescriptor().getValues().get(this.index);
                }

                @Override
                public final com.google.protobuf.Descriptors.EnumDescriptor getDescriptorForType() {
                    return getDescriptor();
                }

                public static final com.google.protobuf.Descriptors.EnumDescriptor getDescriptor() {
                    return org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric
                            .getDescriptor().getEnumTypes().get(0);
                }

                private static final ValueType[] VALUES = values();

                public static ValueType valueOf(com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
                    if (desc.getType() != getDescriptor()) {
                        throw new java.lang.IllegalArgumentException("EnumValueDescriptor is not for this type.");
                    }
                    return VALUES[desc.getIndex()];
                }

                private final int index;
                private final int value;

                private ValueType(int index, int value) {
                    this.index = index;
                    this.value = value;
                }

                // @@protoc_insertion_point(enum_scope:kuradatatypes.KuraPayload.KuraMetric.ValueType)
            }

            private int bitField0;
            public static final int NAME_FIELD_NUMBER = 1;
            private java.lang.Object name;

            /**
             * <code>required string name = 1;</code>
             */
            @Override
            public boolean hasName() {
                return (this.bitField0 & 0x00000001) == 0x00000001;
            }

            /**
             * <code>required string name = 1;</code>
             */
            @Override
            public java.lang.String getName() {
                java.lang.Object ref = this.name;
                if (ref instanceof java.lang.String) {
                    return (java.lang.String) ref;
                } else {
                    com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
                    java.lang.String s = bs.toStringUtf8();
                    if (bs.isValidUtf8()) {
                        this.name = s;
                    }
                    return s;
                }
            }

            /**
             * <code>required string name = 1;</code>
             */
            @Override
            public com.google.protobuf.ByteString getNameBytes() {
                java.lang.Object ref = this.name;
                if (ref instanceof java.lang.String) {
                    com.google.protobuf.ByteString b = com.google.protobuf.ByteString
                            .copyFromUtf8((java.lang.String) ref);
                    this.name = b;
                    return b;
                } else {
                    return (com.google.protobuf.ByteString) ref;
                }
            }

            public static final int TYPE_FIELD_NUMBER = 2;
            private org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.ValueType typeI;

            /**
             * <code>required .kuradatatypes.KuraPayload.KuraMetric.ValueType type = 2;</code>
             */
            @Override
            public boolean hasType() {
                return (this.bitField0 & 0x00000002) == 0x00000002;
            }

            /**
             * <code>required .kuradatatypes.KuraPayload.KuraMetric.ValueType type = 2;</code>
             */
            @Override
            public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.ValueType getType() {
                return this.typeI;
            }

            public static final int DOUBLE_VALUE_FIELD_NUMBER = 3;
            private double doubleValue;

            /**
             * <code>optional double double_value = 3;</code>
             */
            @Override
            public boolean hasDoubleValue() {
                return (this.bitField0 & 0x00000004) == 0x00000004;
            }

            /**
             * <code>optional double double_value = 3;</code>
             */
            @Override
            public double getDoubleValue() {
                return this.doubleValue;
            }

            public static final int FLOAT_VALUE_FIELD_NUMBER = 4;
            private float floatValue;

            /**
             * <code>optional float float_value = 4;</code>
             */
            @Override
            public boolean hasFloatValue() {
                return (this.bitField0 & 0x00000008) == 0x00000008;
            }

            /**
             * <code>optional float float_value = 4;</code>
             */
            @Override
            public float getFloatValue() {
                return this.floatValue;
            }

            public static final int LONG_VALUE_FIELD_NUMBER = 5;
            private long longValue;

            /**
             * <code>optional int64 long_value = 5;</code>
             */
            @Override
            public boolean hasLongValue() {
                return (this.bitField0 & 0x00000010) == 0x00000010;
            }

            /**
             * <code>optional int64 long_value = 5;</code>
             */
            @Override
            public long getLongValue() {
                return this.longValue;
            }

            public static final int INT_VALUE_FIELD_NUMBER = 6;
            private int intValue;

            /**
             * <code>optional int32 int_value = 6;</code>
             */
            @Override
            public boolean hasIntValue() {
                return (this.bitField0 & 0x00000020) == 0x00000020;
            }

            /**
             * <code>optional int32 int_value = 6;</code>
             */
            @Override
            public int getIntValue() {
                return this.intValue;
            }

            public static final int BOOL_VALUE_FIELD_NUMBER = 7;
            private boolean boolValue;

            /**
             * <code>optional bool bool_value = 7;</code>
             */
            @Override
            public boolean hasBoolValue() {
                return (this.bitField0 & 0x00000040) == 0x00000040;
            }

            /**
             * <code>optional bool bool_value = 7;</code>
             */
            @Override
            public boolean getBoolValue() {
                return this.boolValue;
            }

            public static final int STRING_VALUE_FIELD_NUMBER = 8;
            private java.lang.Object stringValue;

            /**
             * <code>optional string string_value = 8;</code>
             */
            @Override
            public boolean hasStringValue() {
                return (this.bitField0 & 0x00000080) == 0x00000080;
            }

            /**
             * <code>optional string string_value = 8;</code>
             */
            @Override
            public java.lang.String getStringValue() {
                java.lang.Object ref = this.stringValue;
                if (ref instanceof java.lang.String) {
                    return (java.lang.String) ref;
                } else {
                    com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
                    java.lang.String s = bs.toStringUtf8();
                    if (bs.isValidUtf8()) {
                        this.stringValue = s;
                    }
                    return s;
                }
            }

            /**
             * <code>optional string string_value = 8;</code>
             */
            @Override
            public com.google.protobuf.ByteString getStringValueBytes() {
                java.lang.Object ref = this.stringValue;
                if (ref instanceof java.lang.String) {
                    com.google.protobuf.ByteString b = com.google.protobuf.ByteString
                            .copyFromUtf8((java.lang.String) ref);
                    this.stringValue = b;
                    return b;
                } else {
                    return (com.google.protobuf.ByteString) ref;
                }
            }

            public static final int BYTES_VALUE_FIELD_NUMBER = 9;
            private com.google.protobuf.ByteString bytesValue;

            /**
             * <code>optional bytes bytes_value = 9;</code>
             */
            @Override
            public boolean hasBytesValue() {
                return (this.bitField0 & 0x00000100) == 0x00000100;
            }

            /**
             * <code>optional bytes bytes_value = 9;</code>
             */
            @Override
            public com.google.protobuf.ByteString getBytesValue() {
                return this.bytesValue;
            }

            private void initFields() {
                this.name = "";
                this.typeI = org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.ValueType.DOUBLE;
                this.doubleValue = 0D;
                this.floatValue = 0F;
                this.longValue = 0L;
                this.intValue = 0;
                this.boolValue = false;
                this.stringValue = "";
                this.bytesValue = com.google.protobuf.ByteString.EMPTY;
            }

            private byte memoizedIsInitialized = -1;

            @Override
            public final boolean isInitialized() {
                byte isInitialized = this.memoizedIsInitialized;
                if (isInitialized == 1) {
                    return true;
                }
                if (isInitialized == 0) {
                    return false;
                }

                if (!hasName()) {
                    this.memoizedIsInitialized = 0;
                    return false;
                }
                if (!hasType()) {
                    this.memoizedIsInitialized = 0;
                    return false;
                }
                this.memoizedIsInitialized = 1;
                return true;
            }

            @Override
            public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
                getSerializedSize();
                if ((this.bitField0 & 0x00000001) == 0x00000001) {
                    output.writeBytes(1, getNameBytes());
                }
                if ((this.bitField0 & 0x00000002) == 0x00000002) {
                    output.writeEnum(2, this.typeI.getNumber());
                }
                if ((this.bitField0 & 0x00000004) == 0x00000004) {
                    output.writeDouble(3, this.doubleValue);
                }
                if ((this.bitField0 & 0x00000008) == 0x00000008) {
                    output.writeFloat(4, this.floatValue);
                }
                if ((this.bitField0 & 0x00000010) == 0x00000010) {
                    output.writeInt64(5, this.longValue);
                }
                if ((this.bitField0 & 0x00000020) == 0x00000020) {
                    output.writeInt32(6, this.intValue);
                }
                if ((this.bitField0 & 0x00000040) == 0x00000040) {
                    output.writeBool(7, this.boolValue);
                }
                if ((this.bitField0 & 0x00000080) == 0x00000080) {
                    output.writeBytes(8, getStringValueBytes());
                }
                if ((this.bitField0 & 0x00000100) == 0x00000100) {
                    output.writeBytes(9, this.bytesValue);
                }
                getUnknownFields().writeTo(output);
            }

            private int memoizedSerializedSize = -1;

            @Override
            public int getSerializedSize() {
                int size = this.memoizedSerializedSize;
                if (size != -1) {
                    return size;
                }

                size = 0;
                if ((this.bitField0 & 0x00000001) == 0x00000001) {
                    size += com.google.protobuf.CodedOutputStream.computeBytesSize(1, getNameBytes());
                }
                if ((this.bitField0 & 0x00000002) == 0x00000002) {
                    size += com.google.protobuf.CodedOutputStream.computeEnumSize(2, this.typeI.getNumber());
                }
                if ((this.bitField0 & 0x00000004) == 0x00000004) {
                    size += com.google.protobuf.CodedOutputStream.computeDoubleSize(3, this.doubleValue);
                }
                if ((this.bitField0 & 0x00000008) == 0x00000008) {
                    size += com.google.protobuf.CodedOutputStream.computeFloatSize(4, this.floatValue);
                }
                if ((this.bitField0 & 0x00000010) == 0x00000010) {
                    size += com.google.protobuf.CodedOutputStream.computeInt64Size(5, this.longValue);
                }
                if ((this.bitField0 & 0x00000020) == 0x00000020) {
                    size += com.google.protobuf.CodedOutputStream.computeInt32Size(6, this.intValue);
                }
                if ((this.bitField0 & 0x00000040) == 0x00000040) {
                    size += com.google.protobuf.CodedOutputStream.computeBoolSize(7, this.boolValue);
                }
                if ((this.bitField0 & 0x00000080) == 0x00000080) {
                    size += com.google.protobuf.CodedOutputStream.computeBytesSize(8, getStringValueBytes());
                }
                if ((this.bitField0 & 0x00000100) == 0x00000100) {
                    size += com.google.protobuf.CodedOutputStream.computeBytesSize(9, this.bytesValue);
                }
                size += getUnknownFields().getSerializedSize();
                this.memoizedSerializedSize = size;
                return size;
            }

            private static final long serialVersionUID = 0L;

            @java.lang.Override
            protected java.lang.Object writeReplace() throws java.io.ObjectStreamException {
                return super.writeReplace();
            }

            public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric parseFrom(
                    com.google.protobuf.ByteString data) throws com.google.protobuf.InvalidProtocolBufferException {
                return PARSER.parseFrom(data);
            }

            public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric parseFrom(
                    com.google.protobuf.ByteString data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                            throws com.google.protobuf.InvalidProtocolBufferException {
                return PARSER.parseFrom(data, extensionRegistry);
            }

            public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric parseFrom(
                    byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
                return PARSER.parseFrom(data);
            }

            public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric parseFrom(
                    byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                            throws com.google.protobuf.InvalidProtocolBufferException {
                return PARSER.parseFrom(data, extensionRegistry);
            }

            public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric parseFrom(
                    java.io.InputStream input) throws java.io.IOException {
                return PARSER.parseFrom(input);
            }

            public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric parseFrom(
                    java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                            throws java.io.IOException {
                return PARSER.parseFrom(input, extensionRegistry);
            }

            public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric parseDelimitedFrom(
                    java.io.InputStream input) throws java.io.IOException {
                return PARSER.parseDelimitedFrom(input);
            }

            public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric parseDelimitedFrom(
                    java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                            throws java.io.IOException {
                return PARSER.parseDelimitedFrom(input, extensionRegistry);
            }

            public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric parseFrom(
                    com.google.protobuf.CodedInputStream input) throws java.io.IOException {
                return PARSER.parseFrom(input);
            }

            public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric parseFrom(
                    com.google.protobuf.CodedInputStream input,
                    com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
                return PARSER.parseFrom(input, extensionRegistry);
            }

            public static Builder newBuilder() {
                return Builder.create();
            }

            @Override
            public Builder newBuilderForType() {
                return newBuilder();
            }

            public static Builder newBuilder(
                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric prototype) {
                return newBuilder().mergeFrom(prototype);
            }

            @Override
            public Builder toBuilder() {
                return newBuilder(this);
            }

            @java.lang.Override
            protected Builder newBuilderForType(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
                Builder builder = new Builder(parent);
                return builder;
            }

            /**
             * Protobuf type {@code kuradatatypes.KuraPayload.KuraMetric}
             */
            public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements
                    // @@protoc_insertion_point(builder_implements:kuradatatypes.KuraPayload.KuraMetric)
                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetricOrBuilder {

                public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
                    return org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.INTERNAL_STATIC_KARADATATYPES_KURAPAYLOAD_KURAMETRIC_DESCRIPTOR;
                }

                @Override
                protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                    return org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.internalStaticKuradatatypesKuraPayloadKuraMetricFieldAccessorTable
                            .ensureFieldAccessorsInitialized(
                                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.class,
                                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.Builder.class);
                }

                // Construct using
                // org.eclipse.kapua.connector.converter.KuraPayloadProto.KuraPayload.KuraMetric.newBuilder()
                private Builder() {
                    maybeForceBuilderInitialization();
                }

                private Builder(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
                    super(parent);
                    maybeForceBuilderInitialization();
                }

                private void maybeForceBuilderInitialization() {
                    if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
                    }
                }

                private static Builder create() {
                    return new Builder();
                }

                @Override
                public Builder clear() {
                    super.clear();
                    this.name = "";
                    this.bitField0 = this.bitField0 & ~0x00000001;
                    this.typeI = org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.ValueType.DOUBLE;
                    this.bitField0 = this.bitField0 & ~0x00000002;
                    this.doubleValue = 0D;
                    this.bitField0 = this.bitField0 & ~0x00000004;
                    this.floatValue = 0F;
                    this.bitField0 = this.bitField0 & ~0x00000008;
                    this.longValue = 0L;
                    this.bitField0 = this.bitField0 & ~0x00000010;
                    this.intValue = 0;
                    this.bitField0 = this.bitField0 & ~0x00000020;
                    this.boolValue = false;
                    this.bitField0 = this.bitField0 & ~0x00000040;
                    this.stringValue = "";
                    this.bitField0 = this.bitField0 & ~0x00000080;
                    this.bytesValue = com.google.protobuf.ByteString.EMPTY;
                    this.bitField0 = this.bitField0 & ~0x00000100;
                    return this;
                }

                @Override
                public Builder clone() {
                    return create().mergeFrom(buildPartial());
                }

                @Override
                public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
                    return org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.INTERNAL_STATIC_KARADATATYPES_KURAPAYLOAD_KURAMETRIC_DESCRIPTOR;
                }

                @Override
                public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric getDefaultInstanceForType() {
                    return org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric
                            .getDefaultInstance();
                }

                @Override
                public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric build() {
                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric result = buildPartial();
                    if (!result.isInitialized()) {
                        throw newUninitializedMessageException(result);
                    }
                    return result;
                }

                @Override
                public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric buildPartial() {
                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric result = new org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric(
                            this);
                    int fromBitField0 = this.bitField0;
                    int toBitField0 = 0;
                    if ((fromBitField0 & 0x00000001) == 0x00000001) {
                        toBitField0 |= 0x00000001;
                    }
                    result.name = this.name;
                    if ((fromBitField0 & 0x00000002) == 0x00000002) {
                        toBitField0 |= 0x00000002;
                    }
                    result.typeI = this.typeI;
                    if ((fromBitField0 & 0x00000004) == 0x00000004) {
                        toBitField0 |= 0x00000004;
                    }
                    result.doubleValue = this.doubleValue;
                    if ((fromBitField0 & 0x00000008) == 0x00000008) {
                        toBitField0 |= 0x00000008;
                    }
                    result.floatValue = this.floatValue;
                    if ((fromBitField0 & 0x00000010) == 0x00000010) {
                        toBitField0 |= 0x00000010;
                    }
                    result.longValue = this.longValue;
                    if ((fromBitField0 & 0x00000020) == 0x00000020) {
                        toBitField0 |= 0x00000020;
                    }
                    result.intValue = this.intValue;
                    if ((fromBitField0 & 0x00000040) == 0x00000040) {
                        toBitField0 |= 0x00000040;
                    }
                    result.boolValue = this.boolValue;
                    if ((fromBitField0 & 0x00000080) == 0x00000080) {
                        toBitField0 |= 0x00000080;
                    }
                    result.stringValue = this.stringValue;
                    if ((fromBitField0 & 0x00000100) == 0x00000100) {
                        toBitField0 |= 0x00000100;
                    }
                    result.bytesValue = this.bytesValue;
                    result.bitField0 = toBitField0;
                    onBuilt();
                    return result;
                }

                @Override
                public Builder mergeFrom(com.google.protobuf.Message other) {
                    if (other instanceof org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric) {
                        return mergeFrom(
                                (org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric) other);
                    } else {
                        super.mergeFrom(other);
                        return this;
                    }
                }

                public Builder mergeFrom(
                        org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric other) {
                    if (other == org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric
                            .getDefaultInstance()) {
                        return this;
                    }
                    if (other.hasName()) {
                        this.bitField0 |= 0x00000001;
                        this.name = other.name;
                        onChanged();
                    }
                    if (other.hasType()) {
                        setType(other.getType());
                    }
                    if (other.hasDoubleValue()) {
                        setDoubleValue(other.getDoubleValue());
                    }
                    if (other.hasFloatValue()) {
                        setFloatValue(other.getFloatValue());
                    }
                    if (other.hasLongValue()) {
                        setLongValue(other.getLongValue());
                    }
                    if (other.hasIntValue()) {
                        setIntValue(other.getIntValue());
                    }
                    if (other.hasBoolValue()) {
                        setBoolValue(other.getBoolValue());
                    }
                    if (other.hasStringValue()) {
                        this.bitField0 |= 0x00000080;
                        this.stringValue = other.stringValue;
                        onChanged();
                    }
                    if (other.hasBytesValue()) {
                        setBytesValue(other.getBytesValue());
                    }
                    mergeUnknownFields(other.getUnknownFields());
                    return this;
                }

                @Override
                public final boolean isInitialized() {
                    if (!hasName()) {

                        return false;
                    }
                    if (!hasType()) {

                        return false;
                    }
                    return true;
                }

                @Override
                public Builder mergeFrom(com.google.protobuf.CodedInputStream input,
                        com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric parsedMessage = null;
                    try {
                        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
                    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                        parsedMessage = (org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric) e
                                .getUnfinishedMessage();
                        throw e;
                    } finally {
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                    }
                    return this;
                }

                private int bitField0;

                private java.lang.Object name = "";

                /**
                 * <code>required string name = 1;</code>
                 */
                @Override
                public boolean hasName() {
                    return (this.bitField0 & 0x00000001) == 0x00000001;
                }

                /**
                 * <code>required string name = 1;</code>
                 */
                @Override
                public java.lang.String getName() {
                    java.lang.Object ref = this.name;
                    if (!(ref instanceof java.lang.String)) {
                        com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
                        java.lang.String s = bs.toStringUtf8();
                        if (bs.isValidUtf8()) {
                            this.name = s;
                        }
                        return s;
                    } else {
                        return (java.lang.String) ref;
                    }
                }

                /**
                 * <code>required string name = 1;</code>
                 */
                @Override
                public com.google.protobuf.ByteString getNameBytes() {
                    java.lang.Object ref = this.name;
                    if (ref instanceof String) {
                        com.google.protobuf.ByteString b = com.google.protobuf.ByteString
                                .copyFromUtf8((java.lang.String) ref);
                        this.name = b;
                        return b;
                    } else {
                        return (com.google.protobuf.ByteString) ref;
                    }
                }

                /**
                 * <code>required string name = 1;</code>
                 */
                public Builder setName(java.lang.String value) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0 |= 0x00000001;
                    this.name = value;
                    onChanged();
                    return this;
                }

                /**
                 * <code>required string name = 1;</code>
                 */
                public Builder clearName() {
                    this.bitField0 = this.bitField0 & ~0x00000001;
                    this.name = getDefaultInstance().getName();
                    onChanged();
                    return this;
                }

                /**
                 * <code>required string name = 1;</code>
                 */
                public Builder setNameBytes(com.google.protobuf.ByteString value) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0 |= 0x00000001;
                    this.name = value;
                    onChanged();
                    return this;
                }

                private org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.ValueType typeI = org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.ValueType.DOUBLE;

                /**
                 * <code>required .kuradatatypes.KuraPayload.KuraMetric.ValueType type = 2;</code>
                 */
                @Override
                public boolean hasType() {
                    return (this.bitField0 & 0x00000002) == 0x00000002;
                }

                /**
                 * <code>required .kuradatatypes.KuraPayload.KuraMetric.ValueType type = 2;</code>
                 */
                @Override
                public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.ValueType getType() {
                    return this.typeI;
                }

                /**
                 * <code>required .kuradatatypes.KuraPayload.KuraMetric.ValueType type = 2;</code>
                 */
                public Builder setType(
                        org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.ValueType value) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0 |= 0x00000002;
                    this.typeI = value;
                    onChanged();
                    return this;
                }

                /**
                 * <code>required .kuradatatypes.KuraPayload.KuraMetric.ValueType type = 2;</code>
                 */
                public Builder clearType() {
                    this.bitField0 = this.bitField0 & ~0x00000002;
                    this.typeI = org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.ValueType.DOUBLE;
                    onChanged();
                    return this;
                }

                private double doubleValue;

                /**
                 * <code>optional double double_value = 3;</code>
                 */
                @Override
                public boolean hasDoubleValue() {
                    return (this.bitField0 & 0x00000004) == 0x00000004;
                }

                /**
                 * <code>optional double double_value = 3;</code>
                 */
                @Override
                public double getDoubleValue() {
                    return this.doubleValue;
                }

                /**
                 * <code>optional double double_value = 3;</code>
                 */
                public Builder setDoubleValue(double value) {
                    this.bitField0 |= 0x00000004;
                    this.doubleValue = value;
                    onChanged();
                    return this;
                }

                /**
                 * <code>optional double double_value = 3;</code>
                 */
                public Builder clearDoubleValue() {
                    this.bitField0 = this.bitField0 & ~0x00000004;
                    this.doubleValue = 0D;
                    onChanged();
                    return this;
                }

                private float floatValue;

                /**
                 * <code>optional float float_value = 4;</code>
                 */
                @Override
                public boolean hasFloatValue() {
                    return (this.bitField0 & 0x00000008) == 0x00000008;
                }

                /**
                 * <code>optional float float_value = 4;</code>
                 */
                @Override
                public float getFloatValue() {
                    return this.floatValue;
                }

                /**
                 * <code>optional float float_value = 4;</code>
                 */
                public Builder setFloatValue(float value) {
                    this.bitField0 |= 0x00000008;
                    this.floatValue = value;
                    onChanged();
                    return this;
                }

                /**
                 * <code>optional float float_value = 4;</code>
                 */
                public Builder clearFloatValue() {
                    this.bitField0 = this.bitField0 & ~0x00000008;
                    this.floatValue = 0F;
                    onChanged();
                    return this;
                }

                private long longValue;

                /**
                 * <code>optional int64 long_value = 5;</code>
                 */
                @Override
                public boolean hasLongValue() {
                    return (this.bitField0 & 0x00000010) == 0x00000010;
                }

                /**
                 * <code>optional int64 long_value = 5;</code>
                 */
                @Override
                public long getLongValue() {
                    return this.longValue;
                }

                /**
                 * <code>optional int64 long_value = 5;</code>
                 */
                public Builder setLongValue(long value) {
                    this.bitField0 |= 0x00000010;
                    this.longValue = value;
                    onChanged();
                    return this;
                }

                /**
                 * <code>optional int64 long_value = 5;</code>
                 */
                public Builder clearLongValue() {
                    this.bitField0 = this.bitField0 & ~0x00000010;
                    this.longValue = 0L;
                    onChanged();
                    return this;
                }

                private int intValue;

                /**
                 * <code>optional int32 int_value = 6;</code>
                 */
                @Override
                public boolean hasIntValue() {
                    return (this.bitField0 & 0x00000020) == 0x00000020;
                }

                /**
                 * <code>optional int32 int_value = 6;</code>
                 */
                @Override
                public int getIntValue() {
                    return this.intValue;
                }

                /**
                 * <code>optional int32 int_value = 6;</code>
                 */
                public Builder setIntValue(int value) {
                    this.bitField0 |= 0x00000020;
                    this.intValue = value;
                    onChanged();
                    return this;
                }

                /**
                 * <code>optional int32 int_value = 6;</code>
                 */
                public Builder clearIntValue() {
                    this.bitField0 = this.bitField0 & ~0x00000020;
                    this.intValue = 0;
                    onChanged();
                    return this;
                }

                private boolean boolValue;

                /**
                 * <code>optional bool bool_value = 7;</code>
                 */
                @Override
                public boolean hasBoolValue() {
                    return (this.bitField0 & 0x00000040) == 0x00000040;
                }

                /**
                 * <code>optional bool bool_value = 7;</code>
                 */
                @Override
                public boolean getBoolValue() {
                    return this.boolValue;
                }

                /**
                 * <code>optional bool bool_value = 7;</code>
                 */
                public Builder setBoolValue(boolean value) {
                    this.bitField0 |= 0x00000040;
                    this.boolValue = value;
                    onChanged();
                    return this;
                }

                /**
                 * <code>optional bool bool_value = 7;</code>
                 */
                public Builder clearBoolValue() {
                    this.bitField0 = this.bitField0 & ~0x00000040;
                    this.boolValue = false;
                    onChanged();
                    return this;
                }

                private java.lang.Object stringValue = "";

                /**
                 * <code>optional string string_value = 8;</code>
                 */
                @Override
                public boolean hasStringValue() {
                    return (this.bitField0 & 0x00000080) == 0x00000080;
                }

                /**
                 * <code>optional string string_value = 8;</code>
                 */
                @Override
                public java.lang.String getStringValue() {
                    java.lang.Object ref = this.stringValue;
                    if (!(ref instanceof java.lang.String)) {
                        com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
                        java.lang.String s = bs.toStringUtf8();
                        if (bs.isValidUtf8()) {
                            this.stringValue = s;
                        }
                        return s;
                    } else {
                        return (java.lang.String) ref;
                    }
                }

                /**
                 * <code>optional string string_value = 8;</code>
                 */
                @Override
                public com.google.protobuf.ByteString getStringValueBytes() {
                    java.lang.Object ref = this.stringValue;
                    if (ref instanceof String) {
                        com.google.protobuf.ByteString b = com.google.protobuf.ByteString
                                .copyFromUtf8((java.lang.String) ref);
                        this.stringValue = b;
                        return b;
                    } else {
                        return (com.google.protobuf.ByteString) ref;
                    }
                }

                /**
                 * <code>optional string string_value = 8;</code>
                 */
                public Builder setStringValue(java.lang.String value) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0 |= 0x00000080;
                    this.stringValue = value;
                    onChanged();
                    return this;
                }

                /**
                 * <code>optional string string_value = 8;</code>
                 */
                public Builder clearStringValue() {
                    this.bitField0 = this.bitField0 & ~0x00000080;
                    this.stringValue = getDefaultInstance().getStringValue();
                    onChanged();
                    return this;
                }

                /**
                 * <code>optional string string_value = 8;</code>
                 */
                public Builder setStringValueBytes(com.google.protobuf.ByteString value) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0 |= 0x00000080;
                    this.stringValue = value;
                    onChanged();
                    return this;
                }

                private com.google.protobuf.ByteString bytesValue = com.google.protobuf.ByteString.EMPTY;

                /**
                 * <code>optional bytes bytes_value = 9;</code>
                 */
                @Override
                public boolean hasBytesValue() {
                    return (this.bitField0 & 0x00000100) == 0x00000100;
                }

                /**
                 * <code>optional bytes bytes_value = 9;</code>
                 */
                @Override
                public com.google.protobuf.ByteString getBytesValue() {
                    return this.bytesValue;
                }

                /**
                 * <code>optional bytes bytes_value = 9;</code>
                 */
                public Builder setBytesValue(com.google.protobuf.ByteString value) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0 |= 0x00000100;
                    this.bytesValue = value;
                    onChanged();
                    return this;
                }

                /**
                 * <code>optional bytes bytes_value = 9;</code>
                 */
                public Builder clearBytesValue() {
                    this.bitField0 = this.bitField0 & ~0x00000100;
                    this.bytesValue = getDefaultInstance().getBytesValue();
                    onChanged();
                    return this;
                }

                // @@protoc_insertion_point(builder_scope:kuradatatypes.KuraPayload.KuraMetric)
            }

            static {
                DEFAULT_INSTANCE = new KuraMetric(true);
                DEFAULT_INSTANCE.initFields();
            }

            // @@protoc_insertion_point(class_scope:kuradatatypes.KuraPayload.KuraMetric)
        }

        public interface KuraPositionOrBuilder extends
                // @@protoc_insertion_point(interface_extends:kuradatatypes.KuraPayload.KuraPosition)
                com.google.protobuf.MessageOrBuilder {

            /**
             * <code>required double latitude = 1;</code>
             */
            boolean hasLatitude();

            /**
             * <code>required double latitude = 1;</code>
             */
            double getLatitude();

            /**
             * <code>required double longitude = 2;</code>
             */
            boolean hasLongitude();

            /**
             * <code>required double longitude = 2;</code>
             */
            double getLongitude();

            /**
             * <code>optional double altitude = 3;</code>
             */
            boolean hasAltitude();

            /**
             * <code>optional double altitude = 3;</code>
             */
            double getAltitude();

            /**
             * <code>optional double precision = 4;</code>
             *
             * <pre>
             * dilution of precision of the current satellite fix.
             * </pre>
             */
            boolean hasPrecision();

            /**
             * <code>optional double precision = 4;</code>
             *
             * <pre>
             * dilution of precision of the current satellite fix.
             * </pre>
             */
            double getPrecision();

            /**
             * <code>optional double heading = 5;</code>
             *
             * <pre>
             * heading in degrees
             * </pre>
             */
            boolean hasHeading();

            /**
             * <code>optional double heading = 5;</code>
             *
             * <pre>
             * heading in degrees
             * </pre>
             */
            double getHeading();

            /**
             * <code>optional double speed = 6;</code>
             *
             * <pre>
             * meters per second
             * </pre>
             */
            boolean hasSpeed();

            /**
             * <code>optional double speed = 6;</code>
             *
             * <pre>
             * meters per second
             * </pre>
             */
            double getSpeed();

            /**
             * <code>optional int64 timestamp = 7;</code>
             */
            boolean hasTimestamp();

            /**
             * <code>optional int64 timestamp = 7;</code>
             */
            long getTimestamp();

            /**
             * <code>optional int32 satellites = 8;</code>
             *
             * <pre>
             * number satellites locked by the GPS device
             * </pre>
             */
            boolean hasSatellites();

            /**
             * <code>optional int32 satellites = 8;</code>
             *
             * <pre>
             * number satellites locked by the GPS device
             * </pre>
             */
            int getSatellites();

            /**
             * <code>optional int32 status = 9;</code>
             *
             * <pre>
             * status indicator for the GPS data: 1 = no GPS response; 2 = error in response; 4 = valid.
             * </pre>
             */
            boolean hasStatus();

            /**
             * <code>optional int32 status = 9;</code>
             *
             * <pre>
             * status indicator for the GPS data: 1 = no GPS response; 2 = error in response; 4 = valid.
             * </pre>
             */
            int getStatus();
        }

        /**
         * Protobuf type {@code kuradatatypes.KuraPayload.KuraPosition}
         */
        public static final class KuraPosition extends com.google.protobuf.GeneratedMessage implements
                // @@protoc_insertion_point(message_implements:kuradatatypes.KuraPayload.KuraPosition)
                KuraPositionOrBuilder {

            // Use KuraPosition.newBuilder() to construct.
            private KuraPosition(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
                super(builder);
                this.unknownFields = builder.getUnknownFields();
            }

            private KuraPosition(boolean noInit) {
                this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance();
            }

            private static final KuraPosition DEFAULT_INSTANCE;

            public static KuraPosition getDefaultInstance() {
                return DEFAULT_INSTANCE;
            }

            @Override
            public KuraPosition getDefaultInstanceForType() {
                return DEFAULT_INSTANCE;
            }

            private final com.google.protobuf.UnknownFieldSet unknownFields;

            @java.lang.Override
            public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
                return this.unknownFields;
            }

            private KuraPosition(com.google.protobuf.CodedInputStream input,
                    com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                            throws com.google.protobuf.InvalidProtocolBufferException {
                initFields();
                com.google.protobuf.UnknownFieldSet.Builder unknownFields = com.google.protobuf.UnknownFieldSet
                        .newBuilder();
                try {
                    boolean done = false;
                    while (!done) {
                        int tag = input.readTag();
                        switch (tag) {
                        case 0:
                            done = true;
                            break;
                        default: {
                            if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                done = true;
                            }
                            break;
                        }
                        case 9: {
                            this.bitField0 |= 0x00000001;
                            this.latitude = input.readDouble();
                            break;
                        }
                        case 17: {
                            this.bitField0 |= 0x00000002;
                            this.longitude = input.readDouble();
                            break;
                        }
                        case 25: {
                            this.bitField0 |= 0x00000004;
                            this.altitude = input.readDouble();
                            break;
                        }
                        case 33: {
                            this.bitField0 |= 0x00000008;
                            this.precision = input.readDouble();
                            break;
                        }
                        case 41: {
                            this.bitField0 |= 0x00000010;
                            this.heading = input.readDouble();
                            break;
                        }
                        case 49: {
                            this.bitField0 |= 0x00000020;
                            this.speed = input.readDouble();
                            break;
                        }
                        case 56: {
                            this.bitField0 |= 0x00000040;
                            this.timestamp = input.readInt64();
                            break;
                        }
                        case 64: {
                            this.bitField0 |= 0x00000080;
                            this.satellites = input.readInt32();
                            break;
                        }
                        case 72: {
                            this.bitField0 |= 0x00000100;
                            this.status = input.readInt32();
                            break;
                        }
                        }
                    }
                } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                    throw e.setUnfinishedMessage(this);
                } catch (java.io.IOException e) {
                    throw new com.google.protobuf.InvalidProtocolBufferException(e.getMessage())
                            .setUnfinishedMessage(this);
                } finally {
                    this.unknownFields = unknownFields.build();
                    makeExtensionsImmutable();
                }
            }

            public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
                return org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.INTERNAL_STATIC_KARADATATYPES_KURAPAYLOAD_KURAPOSITION_DESCRIPTOR;
            }

            @Override
            protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.internalStaticKuradatatypesKuraPayloadKuraPositionFieldAccessorTable
                        .ensureFieldAccessorsInitialized(
                                org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition.class,
                                org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition.Builder.class);
            }

            public static final com.google.protobuf.Parser<KuraPosition> PARSER = new com.google.protobuf.AbstractParser<KuraPosition>() {

                @Override
                public KuraPosition parsePartialFrom(com.google.protobuf.CodedInputStream input,
                        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                                throws com.google.protobuf.InvalidProtocolBufferException {
                    return new KuraPosition(input, extensionRegistry);
                }
            };

            @java.lang.Override
            public com.google.protobuf.Parser<KuraPosition> getParserForType() {
                return PARSER;
            }

            private int bitField0;
            public static final int LATITUDE_FIELD_NUMBER = 1;
            private double latitude;

            /**
             * <code>required double latitude = 1;</code>
             */
            @Override
            public boolean hasLatitude() {
                return (this.bitField0 & 0x00000001) == 0x00000001;
            }

            /**
             * <code>required double latitude = 1;</code>
             */
            @Override
            public double getLatitude() {
                return this.latitude;
            }

            public static final int LONGITUDE_FIELD_NUMBER = 2;
            private double longitude;

            /**
             * <code>required double longitude = 2;</code>
             */
            @Override
            public boolean hasLongitude() {
                return (this.bitField0 & 0x00000002) == 0x00000002;
            }

            /**
             * <code>required double longitude = 2;</code>
             */
            @Override
            public double getLongitude() {
                return this.longitude;
            }

            public static final int ALTITUDE_FIELD_NUMBER = 3;
            private double altitude;

            /**
             * <code>optional double altitude = 3;</code>
             */
            @Override
            public boolean hasAltitude() {
                return (this.bitField0 & 0x00000004) == 0x00000004;
            }

            /**
             * <code>optional double altitude = 3;</code>
             */
            @Override
            public double getAltitude() {
                return this.altitude;
            }

            public static final int PRECISION_FIELD_NUMBER = 4;
            private double precision;

            /**
             * <code>optional double precision = 4;</code>
             *
             * <pre>
             * dilution of precision of the current satellite fix.
             * </pre>
             */
            @Override
            public boolean hasPrecision() {
                return (this.bitField0 & 0x00000008) == 0x00000008;
            }

            /**
             * <code>optional double precision = 4;</code>
             *
             * <pre>
             * dilution of precision of the current satellite fix.
             * </pre>
             */
            @Override
            public double getPrecision() {
                return this.precision;
            }

            public static final int HEADING_FIELD_NUMBER = 5;
            private double heading;

            /**
             * <code>optional double heading = 5;</code>
             *
             * <pre>
             * heading in degrees
             * </pre>
             */
            @Override
            public boolean hasHeading() {
                return (this.bitField0 & 0x00000010) == 0x00000010;
            }

            /**
             * <code>optional double heading = 5;</code>
             *
             * <pre>
             * heading in degrees
             * </pre>
             */
            @Override
            public double getHeading() {
                return this.heading;
            }

            public static final int SPEED_FIELD_NUMBER = 6;
            private double speed;

            /**
             * <code>optional double speed = 6;</code>
             *
             * <pre>
             * meters per second
             * </pre>
             */
            @Override
            public boolean hasSpeed() {
                return (this.bitField0 & 0x00000020) == 0x00000020;
            }

            /**
             * <code>optional double speed = 6;</code>
             *
             * <pre>
             * meters per second
             * </pre>
             */
            @Override
            public double getSpeed() {
                return this.speed;
            }

            public static final int TIMESTAMP_FIELD_NUMBER = 7;
            private long timestamp;

            /**
             * <code>optional int64 timestamp = 7;</code>
             */
            @Override
            public boolean hasTimestamp() {
                return (this.bitField0 & 0x00000040) == 0x00000040;
            }

            /**
             * <code>optional int64 timestamp = 7;</code>
             */
            @Override
            public long getTimestamp() {
                return this.timestamp;
            }

            public static final int SATELLITES_FIELD_NUMBER = 8;
            private int satellites;

            /**
             * <code>optional int32 satellites = 8;</code>
             *
             * <pre>
             * number satellites locked by the GPS device
             * </pre>
             */
            @Override
            public boolean hasSatellites() {
                return (this.bitField0 & 0x00000080) == 0x00000080;
            }

            /**
             * <code>optional int32 satellites = 8;</code>
             *
             * <pre>
             * number satellites locked by the GPS device
             * </pre>
             */
            @Override
            public int getSatellites() {
                return this.satellites;
            }

            public static final int STATUS_FIELD_NUMBER = 9;
            private int status;

            /**
             * <code>optional int32 status = 9;</code>
             *
             * <pre>
             * status indicator for the GPS data: 1 = no GPS response; 2 = error in response; 4 = valid.
             * </pre>
             */
            @Override
            public boolean hasStatus() {
                return (this.bitField0 & 0x00000100) == 0x00000100;
            }

            /**
             * <code>optional int32 status = 9;</code>
             *
             * <pre>
             * status indicator for the GPS data: 1 = no GPS response; 2 = error in response; 4 = valid.
             * </pre>
             */
            @Override
            public int getStatus() {
                return this.status;
            }

            private void initFields() {
                this.latitude = 0D;
                this.longitude = 0D;
                this.altitude = 0D;
                this.precision = 0D;
                this.heading = 0D;
                this.speed = 0D;
                this.timestamp = 0L;
                this.satellites = 0;
                this.status = 0;
            }

            private byte memoizedIsInitialized = -1;

            @Override
            public final boolean isInitialized() {
                byte isInitialized = this.memoizedIsInitialized;
                if (isInitialized == 1) {
                    return true;
                }
                if (isInitialized == 0) {
                    return false;
                }

                if (!hasLatitude()) {
                    this.memoizedIsInitialized = 0;
                    return false;
                }
                if (!hasLongitude()) {
                    this.memoizedIsInitialized = 0;
                    return false;
                }
                this.memoizedIsInitialized = 1;
                return true;
            }

            @Override
            public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
                getSerializedSize();
                if ((this.bitField0 & 0x00000001) == 0x00000001) {
                    output.writeDouble(1, this.latitude);
                }
                if ((this.bitField0 & 0x00000002) == 0x00000002) {
                    output.writeDouble(2, this.longitude);
                }
                if ((this.bitField0 & 0x00000004) == 0x00000004) {
                    output.writeDouble(3, this.altitude);
                }
                if ((this.bitField0 & 0x00000008) == 0x00000008) {
                    output.writeDouble(4, this.precision);
                }
                if ((this.bitField0 & 0x00000010) == 0x00000010) {
                    output.writeDouble(5, this.heading);
                }
                if ((this.bitField0 & 0x00000020) == 0x00000020) {
                    output.writeDouble(6, this.speed);
                }
                if ((this.bitField0 & 0x00000040) == 0x00000040) {
                    output.writeInt64(7, this.timestamp);
                }
                if ((this.bitField0 & 0x00000080) == 0x00000080) {
                    output.writeInt32(8, this.satellites);
                }
                if ((this.bitField0 & 0x00000100) == 0x00000100) {
                    output.writeInt32(9, this.status);
                }
                getUnknownFields().writeTo(output);
            }

            private int memoizedSerializedSize = -1;

            @Override
            public int getSerializedSize() {
                int size = this.memoizedSerializedSize;
                if (size != -1) {
                    return size;
                }

                size = 0;
                if ((this.bitField0 & 0x00000001) == 0x00000001) {
                    size += com.google.protobuf.CodedOutputStream.computeDoubleSize(1, this.latitude);
                }
                if ((this.bitField0 & 0x00000002) == 0x00000002) {
                    size += com.google.protobuf.CodedOutputStream.computeDoubleSize(2, this.longitude);
                }
                if ((this.bitField0 & 0x00000004) == 0x00000004) {
                    size += com.google.protobuf.CodedOutputStream.computeDoubleSize(3, this.altitude);
                }
                if ((this.bitField0 & 0x00000008) == 0x00000008) {
                    size += com.google.protobuf.CodedOutputStream.computeDoubleSize(4, this.precision);
                }
                if ((this.bitField0 & 0x00000010) == 0x00000010) {
                    size += com.google.protobuf.CodedOutputStream.computeDoubleSize(5, this.heading);
                }
                if ((this.bitField0 & 0x00000020) == 0x00000020) {
                    size += com.google.protobuf.CodedOutputStream.computeDoubleSize(6, this.speed);
                }
                if ((this.bitField0 & 0x00000040) == 0x00000040) {
                    size += com.google.protobuf.CodedOutputStream.computeInt64Size(7, this.timestamp);
                }
                if ((this.bitField0 & 0x00000080) == 0x00000080) {
                    size += com.google.protobuf.CodedOutputStream.computeInt32Size(8, this.satellites);
                }
                if ((this.bitField0 & 0x00000100) == 0x00000100) {
                    size += com.google.protobuf.CodedOutputStream.computeInt32Size(9, this.status);
                }
                size += getUnknownFields().getSerializedSize();
                this.memoizedSerializedSize = size;
                return size;
            }

            private static final long serialVersionUID = 0L;

            @java.lang.Override
            protected java.lang.Object writeReplace() throws java.io.ObjectStreamException {
                return super.writeReplace();
            }

            public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition parseFrom(
                    com.google.protobuf.ByteString data) throws com.google.protobuf.InvalidProtocolBufferException {
                return PARSER.parseFrom(data);
            }

            public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition parseFrom(
                    com.google.protobuf.ByteString data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                            throws com.google.protobuf.InvalidProtocolBufferException {
                return PARSER.parseFrom(data, extensionRegistry);
            }

            public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition parseFrom(
                    byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
                return PARSER.parseFrom(data);
            }

            public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition parseFrom(
                    byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                            throws com.google.protobuf.InvalidProtocolBufferException {
                return PARSER.parseFrom(data, extensionRegistry);
            }

            public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition parseFrom(
                    java.io.InputStream input) throws java.io.IOException {
                return PARSER.parseFrom(input);
            }

            public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition parseFrom(
                    java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                            throws java.io.IOException {
                return PARSER.parseFrom(input, extensionRegistry);
            }

            public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition parseDelimitedFrom(
                    java.io.InputStream input) throws java.io.IOException {
                return PARSER.parseDelimitedFrom(input);
            }

            public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition parseDelimitedFrom(
                    java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                            throws java.io.IOException {
                return PARSER.parseDelimitedFrom(input, extensionRegistry);
            }

            public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition parseFrom(
                    com.google.protobuf.CodedInputStream input) throws java.io.IOException {
                return PARSER.parseFrom(input);
            }

            public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition parseFrom(
                    com.google.protobuf.CodedInputStream input,
                    com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
                return PARSER.parseFrom(input, extensionRegistry);
            }

            public static Builder newBuilder() {
                return Builder.create();
            }

            @Override
            public Builder newBuilderForType() {
                return newBuilder();
            }

            public static Builder newBuilder(
                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition prototype) {
                return newBuilder().mergeFrom(prototype);
            }

            @Override
            public Builder toBuilder() {
                return newBuilder(this);
            }

            @java.lang.Override
            protected Builder newBuilderForType(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
                Builder builder = new Builder(parent);
                return builder;
            }

            /**
             * Protobuf type {@code kuradatatypes.KuraPayload.KuraPosition}
             */
            public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements
                    // @@protoc_insertion_point(builder_implements:kuradatatypes.KuraPayload.KuraPosition)
                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPositionOrBuilder {

                public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
                    return org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.INTERNAL_STATIC_KARADATATYPES_KURAPAYLOAD_KURAPOSITION_DESCRIPTOR;
                }

                @Override
                protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                    return org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.internalStaticKuradatatypesKuraPayloadKuraPositionFieldAccessorTable
                            .ensureFieldAccessorsInitialized(
                                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition.class,
                                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition.Builder.class);
                }

                // Construct using
                // org.eclipse.kapua.connector.converter.KuraPayloadProto.KuraPayload.KuraPosition.newBuilder()
                private Builder() {
                    maybeForceBuilderInitialization();
                }

                private Builder(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
                    super(parent);
                    maybeForceBuilderInitialization();
                }

                private void maybeForceBuilderInitialization() {
                    if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
                    }
                }

                private static Builder create() {
                    return new Builder();
                }

                @Override
                public Builder clear() {
                    super.clear();
                    this.latitude = 0D;
                    this.bitField0 = this.bitField0 & ~0x00000001;
                    this.longitude = 0D;
                    this.bitField0 = this.bitField0 & ~0x00000002;
                    this.altitude = 0D;
                    this.bitField0 = this.bitField0 & ~0x00000004;
                    this.precision = 0D;
                    this.bitField0 = this.bitField0 & ~0x00000008;
                    this.heading = 0D;
                    this.bitField0 = this.bitField0 & ~0x00000010;
                    this.speed = 0D;
                    this.bitField0 = this.bitField0 & ~0x00000020;
                    this.timestamp = 0L;
                    this.bitField0 = this.bitField0 & ~0x00000040;
                    this.satellites = 0;
                    this.bitField0 = this.bitField0 & ~0x00000080;
                    this.status = 0;
                    this.bitField0 = this.bitField0 & ~0x00000100;
                    return this;
                }

                @Override
                public Builder clone() {
                    return create().mergeFrom(buildPartial());
                }

                @Override
                public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
                    return org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.INTERNAL_STATIC_KARADATATYPES_KURAPAYLOAD_KURAPOSITION_DESCRIPTOR;
                }

                @Override
                public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition getDefaultInstanceForType() {
                    return org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition
                            .getDefaultInstance();
                }

                @Override
                public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition build() {
                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition result = buildPartial();
                    if (!result.isInitialized()) {
                        throw newUninitializedMessageException(result);
                    }
                    return result;
                }

                @Override
                public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition buildPartial() {
                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition result = new org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition(
                            this);
                    int fromBitField0 = this.bitField0;
                    int toBitField0 = 0;
                    if ((fromBitField0 & 0x00000001) == 0x00000001) {
                        toBitField0 |= 0x00000001;
                    }
                    result.latitude = this.latitude;
                    if ((fromBitField0 & 0x00000002) == 0x00000002) {
                        toBitField0 |= 0x00000002;
                    }
                    result.longitude = this.longitude;
                    if ((fromBitField0 & 0x00000004) == 0x00000004) {
                        toBitField0 |= 0x00000004;
                    }
                    result.altitude = this.altitude;
                    if ((fromBitField0 & 0x00000008) == 0x00000008) {
                        toBitField0 |= 0x00000008;
                    }
                    result.precision = this.precision;
                    if ((fromBitField0 & 0x00000010) == 0x00000010) {
                        toBitField0 |= 0x00000010;
                    }
                    result.heading = this.heading;
                    if ((fromBitField0 & 0x00000020) == 0x00000020) {
                        toBitField0 |= 0x00000020;
                    }
                    result.speed = this.speed;
                    if ((fromBitField0 & 0x00000040) == 0x00000040) {
                        toBitField0 |= 0x00000040;
                    }
                    result.timestamp = this.timestamp;
                    if ((fromBitField0 & 0x00000080) == 0x00000080) {
                        toBitField0 |= 0x00000080;
                    }
                    result.satellites = this.satellites;
                    if ((fromBitField0 & 0x00000100) == 0x00000100) {
                        toBitField0 |= 0x00000100;
                    }
                    result.status = this.status;
                    result.bitField0 = toBitField0;
                    onBuilt();
                    return result;
                }

                @Override
                public Builder mergeFrom(com.google.protobuf.Message other) {
                    if (other instanceof org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition) {
                        return mergeFrom(
                                (org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition) other);
                    } else {
                        super.mergeFrom(other);
                        return this;
                    }
                }

                public Builder mergeFrom(
                        org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition other) {
                    if (other == org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition
                            .getDefaultInstance()) {
                        return this;
                    }
                    if (other.hasLatitude()) {
                        setLatitude(other.getLatitude());
                    }
                    if (other.hasLongitude()) {
                        setLongitude(other.getLongitude());
                    }
                    if (other.hasAltitude()) {
                        setAltitude(other.getAltitude());
                    }
                    if (other.hasPrecision()) {
                        setPrecision(other.getPrecision());
                    }
                    if (other.hasHeading()) {
                        setHeading(other.getHeading());
                    }
                    if (other.hasSpeed()) {
                        setSpeed(other.getSpeed());
                    }
                    if (other.hasTimestamp()) {
                        setTimestamp(other.getTimestamp());
                    }
                    if (other.hasSatellites()) {
                        setSatellites(other.getSatellites());
                    }
                    if (other.hasStatus()) {
                        setStatus(other.getStatus());
                    }
                    mergeUnknownFields(other.getUnknownFields());
                    return this;
                }

                @Override
                public final boolean isInitialized() {
                    if (!hasLatitude()) {

                        return false;
                    }
                    if (!hasLongitude()) {

                        return false;
                    }
                    return true;
                }

                @Override
                public Builder mergeFrom(com.google.protobuf.CodedInputStream input,
                        com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition parsedMessage = null;
                    try {
                        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
                    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                        parsedMessage = (org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition) e
                                .getUnfinishedMessage();
                        throw e;
                    } finally {
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                    }
                    return this;
                }

                private int bitField0;

                private double latitude;

                /**
                 * <code>required double latitude = 1;</code>
                 */
                @Override
                public boolean hasLatitude() {
                    return (this.bitField0 & 0x00000001) == 0x00000001;
                }

                /**
                 * <code>required double latitude = 1;</code>
                 */
                @Override
                public double getLatitude() {
                    return this.latitude;
                }

                /**
                 * <code>required double latitude = 1;</code>
                 */
                public Builder setLatitude(double value) {
                    this.bitField0 |= 0x00000001;
                    this.latitude = value;
                    onChanged();
                    return this;
                }

                /**
                 * <code>required double latitude = 1;</code>
                 */
                public Builder clearLatitude() {
                    this.bitField0 = this.bitField0 & ~0x00000001;
                    this.latitude = 0D;
                    onChanged();
                    return this;
                }

                private double longitude;

                /**
                 * <code>required double longitude = 2;</code>
                 */
                @Override
                public boolean hasLongitude() {
                    return (this.bitField0 & 0x00000002) == 0x00000002;
                }

                /**
                 * <code>required double longitude = 2;</code>
                 */
                @Override
                public double getLongitude() {
                    return this.longitude;
                }

                /**
                 * <code>required double longitude = 2;</code>
                 */
                public Builder setLongitude(double value) {
                    this.bitField0 |= 0x00000002;
                    this.longitude = value;
                    onChanged();
                    return this;
                }

                /**
                 * <code>required double longitude = 2;</code>
                 */
                public Builder clearLongitude() {
                    this.bitField0 = this.bitField0 & ~0x00000002;
                    this.longitude = 0D;
                    onChanged();
                    return this;
                }

                private double altitude;

                /**
                 * <code>optional double altitude = 3;</code>
                 */
                @Override
                public boolean hasAltitude() {
                    return (this.bitField0 & 0x00000004) == 0x00000004;
                }

                /**
                 * <code>optional double altitude = 3;</code>
                 */
                @Override
                public double getAltitude() {
                    return this.altitude;
                }

                /**
                 * <code>optional double altitude = 3;</code>
                 */
                public Builder setAltitude(double value) {
                    this.bitField0 |= 0x00000004;
                    this.altitude = value;
                    onChanged();
                    return this;
                }

                /**
                 * <code>optional double altitude = 3;</code>
                 */
                public Builder clearAltitude() {
                    this.bitField0 = this.bitField0 & ~0x00000004;
                    this.altitude = 0D;
                    onChanged();
                    return this;
                }

                private double precision;

                /**
                 * <code>optional double precision = 4;</code>
                 *
                 * <pre>
                 * dilution of precision of the current satellite fix.
                 * </pre>
                 */
                @Override
                public boolean hasPrecision() {
                    return (this.bitField0 & 0x00000008) == 0x00000008;
                }

                /**
                 * <code>optional double precision = 4;</code>
                 *
                 * <pre>
                 * dilution of precision of the current satellite fix.
                 * </pre>
                 */
                @Override
                public double getPrecision() {
                    return this.precision;
                }

                /**
                 * <code>optional double precision = 4;</code>
                 *
                 * <pre>
                 * dilution of precision of the current satellite fix.
                 * </pre>
                 */
                public Builder setPrecision(double value) {
                    this.bitField0 |= 0x00000008;
                    this.precision = value;
                    onChanged();
                    return this;
                }

                /**
                 * <code>optional double precision = 4;</code>
                 *
                 * <pre>
                 * dilution of precision of the current satellite fix.
                 * </pre>
                 */
                public Builder clearPrecision() {
                    this.bitField0 = this.bitField0 & ~0x00000008;
                    this.precision = 0D;
                    onChanged();
                    return this;
                }

                private double heading;

                /**
                 * <code>optional double heading = 5;</code>
                 *
                 * <pre>
                 * heading in degrees
                 * </pre>
                 */
                @Override
                public boolean hasHeading() {
                    return (this.bitField0 & 0x00000010) == 0x00000010;
                }

                /**
                 * <code>optional double heading = 5;</code>
                 *
                 * <pre>
                 * heading in degrees
                 * </pre>
                 */
                @Override
                public double getHeading() {
                    return this.heading;
                }

                /**
                 * <code>optional double heading = 5;</code>
                 *
                 * <pre>
                 * heading in degrees
                 * </pre>
                 */
                public Builder setHeading(double value) {
                    this.bitField0 |= 0x00000010;
                    this.heading = value;
                    onChanged();
                    return this;
                }

                /**
                 * <code>optional double heading = 5;</code>
                 *
                 * <pre>
                 * heading in degrees
                 * </pre>
                 */
                public Builder clearHeading() {
                    this.bitField0 = this.bitField0 & ~0x00000010;
                    this.heading = 0D;
                    onChanged();
                    return this;
                }

                private double speed;

                /**
                 * <code>optional double speed = 6;</code>
                 *
                 * <pre>
                 * meters per second
                 * </pre>
                 */
                @Override
                public boolean hasSpeed() {
                    return (this.bitField0 & 0x00000020) == 0x00000020;
                }

                /**
                 * <code>optional double speed = 6;</code>
                 *
                 * <pre>
                 * meters per second
                 * </pre>
                 */
                @Override
                public double getSpeed() {
                    return this.speed;
                }

                /**
                 * <code>optional double speed = 6;</code>
                 *
                 * <pre>
                 * meters per second
                 * </pre>
                 */
                public Builder setSpeed(double value) {
                    this.bitField0 |= 0x00000020;
                    this.speed = value;
                    onChanged();
                    return this;
                }

                /**
                 * <code>optional double speed = 6;</code>
                 *
                 * <pre>
                 * meters per second
                 * </pre>
                 */
                public Builder clearSpeed() {
                    this.bitField0 = this.bitField0 & ~0x00000020;
                    this.speed = 0D;
                    onChanged();
                    return this;
                }

                private long timestamp;

                /**
                 * <code>optional int64 timestamp = 7;</code>
                 */
                @Override
                public boolean hasTimestamp() {
                    return (this.bitField0 & 0x00000040) == 0x00000040;
                }

                /**
                 * <code>optional int64 timestamp = 7;</code>
                 */
                @Override
                public long getTimestamp() {
                    return this.timestamp;
                }

                /**
                 * <code>optional int64 timestamp = 7;</code>
                 */
                public Builder setTimestamp(long value) {
                    this.bitField0 |= 0x00000040;
                    this.timestamp = value;
                    onChanged();
                    return this;
                }

                /**
                 * <code>optional int64 timestamp = 7;</code>
                 */
                public Builder clearTimestamp() {
                    this.bitField0 = this.bitField0 & ~0x00000040;
                    this.timestamp = 0L;
                    onChanged();
                    return this;
                }

                private int satellites;

                /**
                 * <code>optional int32 satellites = 8;</code>
                 *
                 * <pre>
                 * number satellites locked by the GPS device
                 * </pre>
                 */
                @Override
                public boolean hasSatellites() {
                    return (this.bitField0 & 0x00000080) == 0x00000080;
                }

                /**
                 * <code>optional int32 satellites = 8;</code>
                 *
                 * <pre>
                 * number satellites locked by the GPS device
                 * </pre>
                 */
                @Override
                public int getSatellites() {
                    return this.satellites;
                }

                /**
                 * <code>optional int32 satellites = 8;</code>
                 *
                 * <pre>
                 * number satellites locked by the GPS device
                 * </pre>
                 */
                public Builder setSatellites(int value) {
                    this.bitField0 |= 0x00000080;
                    this.satellites = value;
                    onChanged();
                    return this;
                }

                /**
                 * <code>optional int32 satellites = 8;</code>
                 *
                 * <pre>
                 * number satellites locked by the GPS device
                 * </pre>
                 */
                public Builder clearSatellites() {
                    this.bitField0 = this.bitField0 & ~0x00000080;
                    this.satellites = 0;
                    onChanged();
                    return this;
                }

                private int status;

                /**
                 * <code>optional int32 status = 9;</code>
                 *
                 * <pre>
                 * status indicator for the GPS data: 1 = no GPS response; 2 = error in response; 4 = valid.
                 * </pre>
                 */
                @Override
                public boolean hasStatus() {
                    return (this.bitField0 & 0x00000100) == 0x00000100;
                }

                /**
                 * <code>optional int32 status = 9;</code>
                 *
                 * <pre>
                 * status indicator for the GPS data: 1 = no GPS response; 2 = error in response; 4 = valid.
                 * </pre>
                 */
                @Override
                public int getStatus() {
                    return this.status;
                }

                /**
                 * <code>optional int32 status = 9;</code>
                 *
                 * <pre>
                 * status indicator for the GPS data: 1 = no GPS response; 2 = error in response; 4 = valid.
                 * </pre>
                 */
                public Builder setStatus(int value) {
                    this.bitField0 |= 0x00000100;
                    this.status = value;
                    onChanged();
                    return this;
                }

                /**
                 * <code>optional int32 status = 9;</code>
                 *
                 * <pre>
                 * status indicator for the GPS data: 1 = no GPS response; 2 = error in response; 4 = valid.
                 * </pre>
                 */
                public Builder clearStatus() {
                    this.bitField0 = this.bitField0 & ~0x00000100;
                    this.status = 0;
                    onChanged();
                    return this;
                }

                // @@protoc_insertion_point(builder_scope:kuradatatypes.KuraPayload.KuraPosition)
            }

            static {
                DEFAULT_INSTANCE = new KuraPosition(true);
                DEFAULT_INSTANCE.initFields();
            }

            // @@protoc_insertion_point(class_scope:kuradatatypes.KuraPayload.KuraPosition)
        }

        private int bitField0;
        public static final int TIMESTAMP_FIELD_NUMBER = 1;
        private long timestamp;

        /**
         * <code>optional int64 timestamp = 1;</code>
         */
        @Override
        public boolean hasTimestamp() {
            return (this.bitField0 & 0x00000001) == 0x00000001;
        }

        /**
         * <code>optional int64 timestamp = 1;</code>
         */
        @Override
        public long getTimestamp() {
            return this.timestamp;
        }

        public static final int POSITION_FIELD_NUMBER = 2;
        private org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition position;

        /**
         * <code>optional .kuradatatypes.KuraPayload.KuraPosition position = 2;</code>
         */
        @Override
        public boolean hasPosition() {
            return (this.bitField0 & 0x00000002) == 0x00000002;
        }

        /**
         * <code>optional .kuradatatypes.KuraPayload.KuraPosition position = 2;</code>
         */
        @Override
        public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition getPosition() {
            return this.position;
        }

        /**
         * <code>optional .kuradatatypes.KuraPayload.KuraPosition position = 2;</code>
         */
        @Override
        public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPositionOrBuilder getPositionOrBuilder() {
            return this.position;
        }

        public static final int METRIC_FIELD_NUMBER = 5000;
        private java.util.List<org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric> metric;

        /**
         * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
         *
         * <pre>
         * can be zero, so optional
         * </pre>
         */
        @Override
        public java.util.List<org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric> getMetricList() {
            return this.metric;
        }

        /**
         * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
         *
         * <pre>
         * can be zero, so optional
         * </pre>
         */
        @Override
        public java.util.List<? extends org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetricOrBuilder> getMetricOrBuilderList() {
            return this.metric;
        }

        /**
         * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
         *
         * <pre>
         * can be zero, so optional
         * </pre>
         */
        @Override
        public int getMetricCount() {
            return this.metric.size();
        }

        /**
         * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
         *
         * <pre>
         * can be zero, so optional
         * </pre>
         */
        @Override
        public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric getMetric(int index) {
            return this.metric.get(index);
        }

        /**
         * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
         *
         * <pre>
         * can be zero, so optional
         * </pre>
         */
        @Override
        public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetricOrBuilder getMetricOrBuilder(
                int index) {
            return this.metric.get(index);
        }

        public static final int BODY_FIELD_NUMBER = 5001;
        private com.google.protobuf.ByteString body;

        /**
         * <code>optional bytes body = 5001;</code>
         */
        @Override
        public boolean hasBody() {
            return (this.bitField0 & 0x00000004) == 0x00000004;
        }

        /**
         * <code>optional bytes body = 5001;</code>
         */
        @Override
        public com.google.protobuf.ByteString getBody() {
            return this.body;
        }

        private void initFields() {
            this.timestamp = 0L;
            this.position = org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition
                    .getDefaultInstance();
            this.metric = java.util.Collections.emptyList();
            this.body = com.google.protobuf.ByteString.EMPTY;
        }

        private byte memoizedIsInitialized = -1;

        @Override
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized == 1) {
                return true;
            }
            if (isInitialized == 0) {
                return false;
            }

            if (hasPosition()) {
                if (!getPosition().isInitialized()) {
                    this.memoizedIsInitialized = 0;
                    return false;
                }
            }
            for (int i = 0; i < getMetricCount(); i++) {
                if (!getMetric(i).isInitialized()) {
                    this.memoizedIsInitialized = 0;
                    return false;
                }
            }
            if (!extensionsAreInitialized()) {
                this.memoizedIsInitialized = 0;
                return false;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        @Override
        public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
            getSerializedSize();
            com.google.protobuf.GeneratedMessage.ExtendableMessage<org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload>.ExtensionWriter extensionWriter = newExtensionWriter();
            if ((this.bitField0 & 0x00000001) == 0x00000001) {
                output.writeInt64(1, this.timestamp);
            }
            if ((this.bitField0 & 0x00000002) == 0x00000002) {
                output.writeMessage(2, this.position);
            }
            extensionWriter.writeUntil(5000, output);
            for (int i = 0; i < this.metric.size(); i++) {
                output.writeMessage(5000, this.metric.get(i));
            }
            if ((this.bitField0 & 0x00000004) == 0x00000004) {
                output.writeBytes(5001, this.body);
            }
            getUnknownFields().writeTo(output);
        }

        private int memoizedSerializedSize = -1;

        @Override
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }

            size = 0;
            if ((this.bitField0 & 0x00000001) == 0x00000001) {
                size += com.google.protobuf.CodedOutputStream.computeInt64Size(1, this.timestamp);
            }
            if ((this.bitField0 & 0x00000002) == 0x00000002) {
                size += com.google.protobuf.CodedOutputStream.computeMessageSize(2, this.position);
            }
            for (int i = 0; i < this.metric.size(); i++) {
                size += com.google.protobuf.CodedOutputStream.computeMessageSize(5000, this.metric.get(i));
            }
            if ((this.bitField0 & 0x00000004) == 0x00000004) {
                size += com.google.protobuf.CodedOutputStream.computeBytesSize(5001, this.body);
            }
            size += extensionsSerializedSize();
            size += getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = size;
            return size;
        }

        private static final long serialVersionUID = 0L;

        @java.lang.Override
        protected java.lang.Object writeReplace() throws java.io.ObjectStreamException {
            return super.writeReplace();
        }

        public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload parseFrom(
                com.google.protobuf.ByteString data) throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload parseFrom(
                com.google.protobuf.ByteString data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                        throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload parseFrom(byte[] data)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload parseFrom(byte[] data,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                        throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload parseFrom(
                java.io.InputStream input) throws java.io.IOException {
            return PARSER.parseFrom(input);
        }

        public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload parseFrom(
                java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                        throws java.io.IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload parseDelimitedFrom(
                java.io.InputStream input) throws java.io.IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload parseDelimitedFrom(
                java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                        throws java.io.IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload parseFrom(
                com.google.protobuf.CodedInputStream input) throws java.io.IOException {
            return PARSER.parseFrom(input);
        }

        public static org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload parseFrom(
                com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                        throws java.io.IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        @Override
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(
                org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        @Override
        public Builder toBuilder() {
            return newBuilder(this);
        }

        @java.lang.Override
        protected Builder newBuilderForType(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        /**
         * Protobuf type {@code kuradatatypes.KuraPayload}
         */
        public static final class Builder extends
                com.google.protobuf.GeneratedMessage.ExtendableBuilder<org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload, Builder>
                implements
                // @@protoc_insertion_point(builder_implements:kuradatatypes.KuraPayload)
                org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayloadOrBuilder {

            public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
                return org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.INTERNAL_STATIC_KARADATATYPES_KURAPAYLOAD_DESCRIPTOR;
            }

            @Override
            protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.internalStaticKuradatatypesKuraPayloadFieldAccessorTable
                        .ensureFieldAccessorsInitialized(
                                org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.class,
                                org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.Builder.class);
            }

            // Construct using org.eclipse.kapua.connector.converter.KuraPayloadProto.KuraPayload.newBuilder()
            private Builder() {
                maybeForceBuilderInitialization();
            }

            private Builder(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
                super(parent);
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
                    getPositionFieldBuilder();
                    getMetricFieldBuilder();
                }
            }

            private static Builder create() {
                return new Builder();
            }

            @Override
            public Builder clear() {
                super.clear();
                this.timestamp = 0L;
                this.bitField0 = this.bitField0 & ~0x00000001;
                if (this.positionBuilder == null) {
                    this.position = org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition
                            .getDefaultInstance();
                } else {
                    this.positionBuilder.clear();
                }
                this.bitField0 = this.bitField0 & ~0x00000002;
                if (this.metricBuilder == null) {
                    this.metric = java.util.Collections.emptyList();
                    this.bitField0 = this.bitField0 & ~0x00000004;
                } else {
                    this.metricBuilder.clear();
                }
                this.body = com.google.protobuf.ByteString.EMPTY;
                this.bitField0 = this.bitField0 & ~0x00000008;
                return this;
            }

            @Override
            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            @Override
            public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
                return org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.INTERNAL_STATIC_KARADATATYPES_KURAPAYLOAD_DESCRIPTOR;
            }

            @Override
            public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload getDefaultInstanceForType() {
                return org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.getDefaultInstance();
            }

            @Override
            public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload build() {
                org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            @Override
            public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload buildPartial() {
                org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload result = new org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload(
                        this);
                int fromBitField0 = this.bitField0;
                int toBitField0 = 0;
                if ((fromBitField0 & 0x00000001) == 0x00000001) {
                    toBitField0 |= 0x00000001;
                }
                result.timestamp = this.timestamp;
                if ((fromBitField0 & 0x00000002) == 0x00000002) {
                    toBitField0 |= 0x00000002;
                }
                if (this.positionBuilder == null) {
                    result.position = this.position;
                } else {
                    result.position = this.positionBuilder.build();
                }
                if (this.metricBuilder == null) {
                    if ((this.bitField0 & 0x00000004) == 0x00000004) {
                        this.metric = java.util.Collections.unmodifiableList(this.metric);
                        this.bitField0 = this.bitField0 & ~0x00000004;
                    }
                    result.metric = this.metric;
                } else {
                    result.metric = this.metricBuilder.build();
                }
                if ((fromBitField0 & 0x00000008) == 0x00000008) {
                    toBitField0 |= 0x00000004;
                }
                result.body = this.body;
                result.bitField0 = toBitField0;
                onBuilt();
                return result;
            }

            @Override
            public Builder mergeFrom(com.google.protobuf.Message other) {
                if (other instanceof org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload) {
                    return mergeFrom((org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload) other);
                } else {
                    super.mergeFrom(other);
                    return this;
                }
            }

            public Builder mergeFrom(org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload other) {
                if (other == org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.getDefaultInstance()) {
                    return this;
                }
                if (other.hasTimestamp()) {
                    setTimestamp(other.getTimestamp());
                }
                if (other.hasPosition()) {
                    mergePosition(other.getPosition());
                }
                if (this.metricBuilder == null) {
                    if (!other.metric.isEmpty()) {
                        if (this.metric.isEmpty()) {
                            this.metric = other.metric;
                            this.bitField0 = this.bitField0 & ~0x00000004;
                        } else {
                            ensureMetricIsMutable();
                            this.metric.addAll(other.metric);
                        }
                        onChanged();
                    }
                } else {
                    if (!other.metric.isEmpty()) {
                        if (this.metricBuilder.isEmpty()) {
                            this.metricBuilder.dispose();
                            this.metricBuilder = null;
                            this.metric = other.metric;
                            this.bitField0 = this.bitField0 & ~0x00000004;
                            this.metricBuilder = com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders
                                    ? getMetricFieldBuilder() : null;
                        } else {
                            this.metricBuilder.addAllMessages(other.metric);
                        }
                    }
                }
                if (other.hasBody()) {
                    setBody(other.getBody());
                }
                mergeExtensionFields(other);
                mergeUnknownFields(other.getUnknownFields());
                return this;
            }

            @Override
            public final boolean isInitialized() {
                if (hasPosition()) {
                    if (!getPosition().isInitialized()) {

                        return false;
                    }
                }
                for (int i = 0; i < getMetricCount(); i++) {
                    if (!getMetric(i).isInitialized()) {

                        return false;
                    }
                }
                if (!extensionsAreInitialized()) {

                    return false;
                }
                return true;
            }

            @Override
            public Builder mergeFrom(com.google.protobuf.CodedInputStream input,
                    com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
                org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload parsedMessage = null;
                try {
                    parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
                } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                    parsedMessage = (org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload) e
                            .getUnfinishedMessage();
                    throw e;
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
                return this;
            }

            private int bitField0;

            private long timestamp;

            /**
             * <code>optional int64 timestamp = 1;</code>
             */
            @Override
            public boolean hasTimestamp() {
                return (this.bitField0 & 0x00000001) == 0x00000001;
            }

            /**
             * <code>optional int64 timestamp = 1;</code>
             */
            @Override
            public long getTimestamp() {
                return this.timestamp;
            }

            /**
             * <code>optional int64 timestamp = 1;</code>
             */
            public Builder setTimestamp(long value) {
                this.bitField0 |= 0x00000001;
                this.timestamp = value;
                onChanged();
                return this;
            }

            /**
             * <code>optional int64 timestamp = 1;</code>
             */
            public Builder clearTimestamp() {
                this.bitField0 = this.bitField0 & ~0x00000001;
                this.timestamp = 0L;
                onChanged();
                return this;
            }

            private org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition position = org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition
                    .getDefaultInstance();
            private com.google.protobuf.SingleFieldBuilder<org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition, org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition.Builder, org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPositionOrBuilder> positionBuilder;

            /**
             * <code>optional .kuradatatypes.KuraPayload.KuraPosition position = 2;</code>
             */
            @Override
            public boolean hasPosition() {
                return (this.bitField0 & 0x00000002) == 0x00000002;
            }

            /**
             * <code>optional .kuradatatypes.KuraPayload.KuraPosition position = 2;</code>
             */
            @Override
            public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition getPosition() {
                if (this.positionBuilder == null) {
                    return this.position;
                } else {
                    return this.positionBuilder.getMessage();
                }
            }

            /**
             * <code>optional .kuradatatypes.KuraPayload.KuraPosition position = 2;</code>
             */
            public Builder setPosition(
                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition value) {
                if (this.positionBuilder == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.position = value;
                    onChanged();
                } else {
                    this.positionBuilder.setMessage(value);
                }
                this.bitField0 |= 0x00000002;
                return this;
            }

            /**
             * <code>optional .kuradatatypes.KuraPayload.KuraPosition position = 2;</code>
             */
            public Builder setPosition(
                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition.Builder builderForValue) {
                if (this.positionBuilder == null) {
                    this.position = builderForValue.build();
                    onChanged();
                } else {
                    this.positionBuilder.setMessage(builderForValue.build());
                }
                this.bitField0 |= 0x00000002;
                return this;
            }

            /**
             * <code>optional .kuradatatypes.KuraPayload.KuraPosition position = 2;</code>
             */
            public Builder mergePosition(
                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition value) {
                if (this.positionBuilder == null) {
                    if ((this.bitField0 & 0x00000002) == 0x00000002
                            && this.position != org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition
                                    .getDefaultInstance()) {
                        this.position = org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition
                                .newBuilder(this.position).mergeFrom(value).buildPartial();
                    } else {
                        this.position = value;
                    }
                    onChanged();
                } else {
                    this.positionBuilder.mergeFrom(value);
                }
                this.bitField0 |= 0x00000002;
                return this;
            }

            /**
             * <code>optional .kuradatatypes.KuraPayload.KuraPosition position = 2;</code>
             */
            public Builder clearPosition() {
                if (this.positionBuilder == null) {
                    this.position = org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition
                            .getDefaultInstance();
                    onChanged();
                } else {
                    this.positionBuilder.clear();
                }
                this.bitField0 = this.bitField0 & ~0x00000002;
                return this;
            }

            /**
             * <code>optional .kuradatatypes.KuraPayload.KuraPosition position = 2;</code>
             */
            public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition.Builder getPositionBuilder() {
                this.bitField0 |= 0x00000002;
                onChanged();
                return getPositionFieldBuilder().getBuilder();
            }

            /**
             * <code>optional .kuradatatypes.KuraPayload.KuraPosition position = 2;</code>
             */
            @Override
            public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPositionOrBuilder getPositionOrBuilder() {
                if (this.positionBuilder != null) {
                    return this.positionBuilder.getMessageOrBuilder();
                } else {
                    return this.position;
                }
            }

            /**
             * <code>optional .kuradatatypes.KuraPayload.KuraPosition position = 2;</code>
             */
            private com.google.protobuf.SingleFieldBuilder<org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition, org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition.Builder, org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPositionOrBuilder> getPositionFieldBuilder() {
                if (this.positionBuilder == null) {
                    this.positionBuilder = new com.google.protobuf.SingleFieldBuilder<org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition, org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPosition.Builder, org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraPositionOrBuilder>(
                            getPosition(), getParentForChildren(), isClean());
                    this.position = null;
                }
                return this.positionBuilder;
            }

            private java.util.List<org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric> metric = java.util.Collections
                    .emptyList();

            private void ensureMetricIsMutable() {
                if (!((this.bitField0 & 0x00000004) == 0x00000004)) {
                    this.metric = new java.util.ArrayList<org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric>(
                            this.metric);
                    this.bitField0 |= 0x00000004;
                }
            }

            private com.google.protobuf.RepeatedFieldBuilder<org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric, org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.Builder, org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetricOrBuilder> metricBuilder;

            /**
             * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
             *
             * <pre>
             * can be zero, so optional
             * </pre>
             */
            @Override
            public java.util.List<org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric> getMetricList() {
                if (this.metricBuilder == null) {
                    return java.util.Collections.unmodifiableList(this.metric);
                } else {
                    return this.metricBuilder.getMessageList();
                }
            }

            /**
             * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
             *
             * <pre>
             * can be zero, so optional
             * </pre>
             */
            @Override
            public int getMetricCount() {
                if (this.metricBuilder == null) {
                    return this.metric.size();
                } else {
                    return this.metricBuilder.getCount();
                }
            }

            /**
             * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
             *
             * <pre>
             * can be zero, so optional
             * </pre>
             */
            @Override
            public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric getMetric(int index) {
                if (this.metricBuilder == null) {
                    return this.metric.get(index);
                } else {
                    return this.metricBuilder.getMessage(index);
                }
            }

            /**
             * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
             *
             * <pre>
             * can be zero, so optional
             * </pre>
             */
            public Builder setMetric(int index,
                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric value) {
                if (this.metricBuilder == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureMetricIsMutable();
                    this.metric.set(index, value);
                    onChanged();
                } else {
                    this.metricBuilder.setMessage(index, value);
                }
                return this;
            }

            /**
             * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
             *
             * <pre>
             * can be zero, so optional
             * </pre>
             */
            public Builder setMetric(int index,
                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.Builder builderForValue) {
                if (this.metricBuilder == null) {
                    ensureMetricIsMutable();
                    this.metric.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.metricBuilder.setMessage(index, builderForValue.build());
                }
                return this;
            }

            /**
             * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
             *
             * <pre>
             * can be zero, so optional
             * </pre>
             */
            public Builder addMetric(
                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric value) {
                if (this.metricBuilder == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureMetricIsMutable();
                    this.metric.add(value);
                    onChanged();
                } else {
                    this.metricBuilder.addMessage(value);
                }
                return this;
            }

            /**
             * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
             *
             * <pre>
             * can be zero, so optional
             * </pre>
             */
            public Builder addMetric(int index,
                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric value) {
                if (this.metricBuilder == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureMetricIsMutable();
                    this.metric.add(index, value);
                    onChanged();
                } else {
                    this.metricBuilder.addMessage(index, value);
                }
                return this;
            }

            /**
             * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
             *
             * <pre>
             * can be zero, so optional
             * </pre>
             */
            public Builder addMetric(
                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.Builder builderForValue) {
                if (this.metricBuilder == null) {
                    ensureMetricIsMutable();
                    this.metric.add(builderForValue.build());
                    onChanged();
                } else {
                    this.metricBuilder.addMessage(builderForValue.build());
                }
                return this;
            }

            /**
             * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
             *
             * <pre>
             * can be zero, so optional
             * </pre>
             */
            public Builder addMetric(int index,
                    org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.Builder builderForValue) {
                if (this.metricBuilder == null) {
                    ensureMetricIsMutable();
                    this.metric.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.metricBuilder.addMessage(index, builderForValue.build());
                }
                return this;
            }

            /**
             * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
             *
             * <pre>
             * can be zero, so optional
             * </pre>
             */
            public Builder addAllMetric(
                    java.lang.Iterable<? extends org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric> values) {
                if (this.metricBuilder == null) {
                    ensureMetricIsMutable();
                    com.google.protobuf.AbstractMessageLite.Builder.addAll(values, this.metric);
                    onChanged();
                } else {
                    this.metricBuilder.addAllMessages(values);
                }
                return this;
            }

            /**
             * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
             *
             * <pre>
             * can be zero, so optional
             * </pre>
             */
            public Builder clearMetric() {
                if (this.metricBuilder == null) {
                    this.metric = java.util.Collections.emptyList();
                    this.bitField0 = this.bitField0 & ~0x00000004;
                    onChanged();
                } else {
                    this.metricBuilder.clear();
                }
                return this;
            }

            /**
             * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
             *
             * <pre>
             * can be zero, so optional
             * </pre>
             */
            public Builder removeMetric(int index) {
                if (this.metricBuilder == null) {
                    ensureMetricIsMutable();
                    this.metric.remove(index);
                    onChanged();
                } else {
                    this.metricBuilder.remove(index);
                }
                return this;
            }

            /**
             * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
             *
             * <pre>
             * can be zero, so optional
             * </pre>
             */
            public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.Builder getMetricBuilder(
                    int index) {
                return getMetricFieldBuilder().getBuilder(index);
            }

            /**
             * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
             *
             * <pre>
             * can be zero, so optional
             * </pre>
             */
            @Override
            public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetricOrBuilder getMetricOrBuilder(
                    int index) {
                if (this.metricBuilder == null) {
                    return this.metric.get(index);
                } else {
                    return this.metricBuilder.getMessageOrBuilder(index);
                }
            }

            /**
             * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
             *
             * <pre>
             * can be zero, so optional
             * </pre>
             */
            @Override
            public java.util.List<? extends org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetricOrBuilder> getMetricOrBuilderList() {
                if (this.metricBuilder != null) {
                    return this.metricBuilder.getMessageOrBuilderList();
                } else {
                    return java.util.Collections.unmodifiableList(this.metric);
                }
            }

            /**
             * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
             *
             * <pre>
             * can be zero, so optional
             * </pre>
             */
            public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.Builder addMetricBuilder() {
                return getMetricFieldBuilder()
                        .addBuilder(org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric
                                .getDefaultInstance());
            }

            /**
             * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
             *
             * <pre>
             * can be zero, so optional
             * </pre>
             */
            public org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.Builder addMetricBuilder(
                    int index) {
                return getMetricFieldBuilder().addBuilder(index,
                        org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric
                                .getDefaultInstance());
            }

            /**
             * <code>repeated .kuradatatypes.KuraPayload.KuraMetric metric = 5000;</code>
             *
             * <pre>
             * can be zero, so optional
             * </pre>
             */
            public java.util.List<org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.Builder> getMetricBuilderList() {
                return getMetricFieldBuilder().getBuilderList();
            }

            private com.google.protobuf.RepeatedFieldBuilder<org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric, org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.Builder, org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetricOrBuilder> getMetricFieldBuilder() {
                if (this.metricBuilder == null) {
                    this.metricBuilder = new com.google.protobuf.RepeatedFieldBuilder<org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric, org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetric.Builder, org.eclipse.kapua.connector.kura.proto.KuraPayloadProto.KuraPayload.KuraMetricOrBuilder>(
                            this.metric, (this.bitField0 & 0x00000004) == 0x00000004, getParentForChildren(),
                            isClean());
                    this.metric = null;
                }
                return this.metricBuilder;
            }

            private com.google.protobuf.ByteString body = com.google.protobuf.ByteString.EMPTY;

            /**
             * <code>optional bytes body = 5001;</code>
             */
            @Override
            public boolean hasBody() {
                return (this.bitField0 & 0x00000008) == 0x00000008;
            }

            /**
             * <code>optional bytes body = 5001;</code>
             */
            @Override
            public com.google.protobuf.ByteString getBody() {
                return this.body;
            }

            /**
             * <code>optional bytes body = 5001;</code>
             */
            public Builder setBody(com.google.protobuf.ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0 |= 0x00000008;
                this.body = value;
                onChanged();
                return this;
            }

            /**
             * <code>optional bytes body = 5001;</code>
             */
            public Builder clearBody() {
                this.bitField0 = this.bitField0 & ~0x00000008;
                this.body = getDefaultInstance().getBody();
                onChanged();
                return this;
            }

            // @@protoc_insertion_point(builder_scope:kuradatatypes.KuraPayload)
        }

        static {
            DEFAULT_INSTANCE = new KuraPayload(true);
            DEFAULT_INSTANCE.initFields();
        }

        // @@protoc_insertion_point(class_scope:kuradatatypes.KuraPayload)
    }

    private static final com.google.protobuf.Descriptors.Descriptor INTERNAL_STATIC_KARADATATYPES_KURAPAYLOAD_DESCRIPTOR;
    private static com.google.protobuf.GeneratedMessage.FieldAccessorTable internalStaticKuradatatypesKuraPayloadFieldAccessorTable;
    private static final com.google.protobuf.Descriptors.Descriptor INTERNAL_STATIC_KARADATATYPES_KURAPAYLOAD_KURAMETRIC_DESCRIPTOR;
    private static com.google.protobuf.GeneratedMessage.FieldAccessorTable internalStaticKuradatatypesKuraPayloadKuraMetricFieldAccessorTable;
    private static final com.google.protobuf.Descriptors.Descriptor INTERNAL_STATIC_KARADATATYPES_KURAPAYLOAD_KURAPOSITION_DESCRIPTOR;
    private static com.google.protobuf.GeneratedMessage.FieldAccessorTable internalStaticKuradatatypesKuraPayloadKuraPositionFieldAccessorTable;

    public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

    static {
        java.lang.String[] descriptorData = {
                "\n\021kurapayload.proto\022\rkuradatatypes\"\243\005\n\013K"
                        + "uraPayload\022\021\n\ttimestamp\030\001 \001(\003\0229\n\010positio"
                        + "n\030\002 \001(\0132\'.kuradatatypes.KuraPayload.Kura"
                        + "Position\0226\n\006metric\030\210\' \003(\0132%.kuradatatype"
                        + "s.KuraPayload.KuraMetric\022\r\n\004body\030\211\' \001(\014\032"
                        + "\305\002\n\nKuraMetric\022\014\n\004name\030\001 \002(\t\022=\n\004type\030\002 \002"
                        + "(\0162/.kuradatatypes.KuraPayload.KuraMetri"
                        + "c.ValueType\022\024\n\014double_value\030\003 \001(\001\022\023\n\013flo"
                        + "at_value\030\004 \001(\002\022\022\n\nlong_value\030\005 \001(\003\022\021\n\tin"
                        + "t_value\030\006 \001(\005\022\022\n\nbool_value\030\007 \001(\010\022\024\n\014str",
                "ing_value\030\010 \001(\t\022\023\n\013bytes_value\030\t \001(\014\"Y\n\t"
                        + "ValueType\022\n\n\006DOUBLE\020\000\022\t\n\005FLOAT\020\001\022\t\n\005INT6"
                        + "4\020\002\022\t\n\005INT32\020\003\022\010\n\004BOOL\020\004\022\n\n\006STRING\020\005\022\t\n\005"
                        + "BYTES\020\006\032\257\001\n\014KuraPosition\022\020\n\010latitude\030\001 \002"
                        + "(\001\022\021\n\tlongitude\030\002 \002(\001\022\020\n\010altitude\030\003 \001(\001\022"
                        + "\021\n\tprecision\030\004 \001(\001\022\017\n\007heading\030\005 \001(\001\022\r\n\005s"
                        + "peed\030\006 \001(\001\022\021\n\ttimestamp\030\007 \001(\003\022\022\n\nsatelli"
                        + "tes\030\010 \001(\005\022\016\n\006status\030\t \001(\005*\005\010\003\020\210\'B:\n&org."
                        + "eclipse.kura.core.message.protobufB\020Kura" + "PayloadProto" };
        com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner = new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {

            @Override
            public com.google.protobuf.ExtensionRegistry assignDescriptors(
                    com.google.protobuf.Descriptors.FileDescriptor root) {
                descriptor = root;
                return null;
            }
        };
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData,
                new com.google.protobuf.Descriptors.FileDescriptor[] {}, assigner);
        INTERNAL_STATIC_KARADATATYPES_KURAPAYLOAD_DESCRIPTOR = getDescriptor().getMessageTypes().get(0);
        internalStaticKuradatatypesKuraPayloadFieldAccessorTable = new com.google.protobuf.GeneratedMessage.FieldAccessorTable(
                INTERNAL_STATIC_KARADATATYPES_KURAPAYLOAD_DESCRIPTOR,
                new java.lang.String[] { "Timestamp", "Position", "Metric", "Body", });
        INTERNAL_STATIC_KARADATATYPES_KURAPAYLOAD_KURAMETRIC_DESCRIPTOR = INTERNAL_STATIC_KARADATATYPES_KURAPAYLOAD_DESCRIPTOR
                .getNestedTypes().get(0);
        internalStaticKuradatatypesKuraPayloadKuraMetricFieldAccessorTable = new com.google.protobuf.GeneratedMessage.FieldAccessorTable(
                INTERNAL_STATIC_KARADATATYPES_KURAPAYLOAD_KURAMETRIC_DESCRIPTOR,
                new java.lang.String[] { "Name", "Type", "DoubleValue", "FloatValue", "LongValue", "IntValue",
                        "BoolValue", "StringValue", "BytesValue", });
        INTERNAL_STATIC_KARADATATYPES_KURAPAYLOAD_KURAPOSITION_DESCRIPTOR = INTERNAL_STATIC_KARADATATYPES_KURAPAYLOAD_DESCRIPTOR
                .getNestedTypes().get(1);
        internalStaticKuradatatypesKuraPayloadKuraPositionFieldAccessorTable = new com.google.protobuf.GeneratedMessage.FieldAccessorTable(
                INTERNAL_STATIC_KARADATATYPES_KURAPAYLOAD_KURAPOSITION_DESCRIPTOR,
                new java.lang.String[] { "Latitude", "Longitude", "Altitude", "Precision", "Heading", "Speed",
                        "Timestamp", "Satellites", "Status", });
    }

    // @@protoc_insertion_point(outer_class_scope)
}
