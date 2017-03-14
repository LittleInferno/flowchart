package com.littleinferno.flowchart.pin;

public class PinManager {
//
//    public static final PinManager instance = new PinManager();
//
//    private Map<Integer, Pin> pins = new TreeMap<Integer, Pin>();
//    private List<Pin> availableToConnect = new ArrayList<Pin>();
//
//    private int idCounter = 0;
//
//    public Tuple<Integer, Pin> getDataInputPin(final String title, final DataType dataType) {
//        idCounter++;
//
//        pins.put(idCounter, new DataInputPin(title, dataType));
//        return new Tuple<Integer, Pin>(idCounter, pins.get(idCounter));
//    }
//
//    public Tuple<Integer, Pin> getDataOutputPin(final String title, final DataType dataType) {
//        idCounter++;
//
//        pins.put(idCounter, new DataOutputPin(title, dataType));
//        return new Tuple<Integer, Pin>(idCounter, pins.get(idCounter));
//    }
//
//    public Tuple<Integer, Pin> getExecutionInputPin(final String title) {
//        idCounter++;
//
//        pins.put(idCounter, new ExecutionInputPin(title));
//        return new Tuple<Integer, Pin>(idCounter, pins.get(idCounter));
//    }
//
//    public Tuple<Integer, Pin> getExecutionOutputPin(final String title) {
//        idCounter++;
//
//        pins.put(idCounter, new ExecutionOutputPin(title));
//        return new Tuple<Integer, Pin>(idCounter, pins.get(idCounter));
//    }
//
//    public Pin getPin(int id) {
//        return pins.get(id);
//    }
//
//    public void select(Pin pin) {
//
//        if (pin instanceof DataInputPin) {
//            for (Map.Entry i : pins.entrySet()) {
//                Pin value = (Pin) i.getValue();
//                if (value != pin &&
//                        value instanceof DataOutputPin &&
//                        value.isArray() == pin.isArray()) {
//                    availableToConnect.add(value);
//                    value.setAvailableToConnect(true);
//                }
//            }
//        } else if (pin instanceof DataOutputPin) {
//            for (Map.Entry i : pins.entrySet()) {
//                Pin value = (Pin) i.getValue();
//                if (i != pin &&
//                        value instanceof DataInputPin &&
//                        !value.isConnect() &&
//                        value.isArray() == pin.isArray()) {
//                    availableToConnect.add(value);
//                    value.setAvailableToConnect(true);
//                }
//            }
//        } else if (pin instanceof ExecutionOutputPin) {
//            for (Map.Entry i : pins.entrySet()) {
//                Pin value = (Pin) i.getValue();
//                if (value != pin &&
//                        value instanceof ExecutionInputPin &&
//                        value.isArray() == pin.isArray()) {
//                    availableToConnect.add(value);
//                    value.setAvailableToConnect(true);
//                }
//            }
//        } else {
//            for (Map.Entry i : pins.entrySet()) {
//                Pin value = (Pin) i.getValue();
//                if (value != pin &&
//                        value instanceof ExecutionOutputPin &&
//                        !value.isConnect() &&
//                        value.isArray() == pin.isArray()) {
//                    availableToConnect.add(value);
//                    value.setAvailableToConnect(true);
//                }
//            }
//        }
//    }
//
//    void unselect() {
//        for (Pin i : availableToConnect) {
//            i.setAvailableToConnect(false);
//        }
//        availableToConnect.clear();
//    }

}
