package com.littleinferno.flowchart;

public enum DataType implements DataTypeInterface {
    EXECUTION {
        @Override
        public String getDefaultValue() {
            return null;
        }
    },
    BOOL {
        @Override
        public String getDefaultValue() {
            return "false";
        }
    },
    INT {
        @Override
        public String getDefaultValue() {
            return String.valueOf(0);
        }
    },
    FLOAT {
        @Override
        public String getDefaultValue() {
            return String.valueOf(0.0f);
        }
    },
    STRING {
        @Override
        public String getDefaultValue() {
            return "";
        }
    },
    UNIVERSAL {
        @Override
        public String getDefaultValue() {
            return null;
        }
    };

}
