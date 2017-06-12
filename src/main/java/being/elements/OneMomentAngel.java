package being.elements;

import being.God;
import being.ioWorker.FileWorker;
import being.mathematics.ThreeVector;
import being.physics.Physics;
import being.physics.PhysicsConfigurations;
import being.universe.AbstractUniverse;
import being.universe.UniverseStage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Serega on 22.05.2017.
 */
public class OneMomentAngel extends Angel {
    private static final int INPUT_SAMPLES_AMOUNT = 3;
    private static final int OUTPUT_SAMPLES_AMOUNT = 1;
    private static final int INPUT_SAMPLE_SIZE_AMOUNT = 5;
    private static final int OUTPUT_SAMPLE_SIZE_AMOUNT = 3;

    private int inputSampleCounter = 0;
    private int outputSampleCounter = 0;
    private double[] inputSample = new double[INPUT_SAMPLES_AMOUNT * INPUT_SAMPLE_SIZE_AMOUNT];
    private double[] outputSample = new double[OUTPUT_SAMPLES_AMOUNT * OUTPUT_SAMPLE_SIZE_AMOUNT];

    //todo
    private final SampleWriter sampleWriter = new SampleWriter();
    private static final long TARGET_OBJECT_WRITTEN_FIELDS_ZERO_INPUT_SAMPLE = SampleWriter.ACCELERATION;
    private static final long OTHER_OBJECT_WRITTEN_FIELDS_ZERO_INPUT_SAMPLE = SampleWriter.EMPTY;
    private static final long TARGET_OBJECT_WRITTEN_FIELDS_FIRST_INPUT_SAMPLE = SampleWriter.POSITION | SampleWriter.ACCELERATION;
    private static final long OTHER_OBJECT_WRITTEN_FIELDS_FIRST_INPUT_SAMPLE = SampleWriter.EMPTY;
    private static final long TARGET_OBJECT_WRITTEN_FIELDS_SECOND_INPUT_SAMPLE = SampleWriter.EMPTY;
    private static final long OTHER_OBJECT_WRITTEN_FIELDS_SECOND_INPUT_SAMPLE = SampleWriter.EMPTY;

    private static final long TARGET_OBJECT_WRITTEN_FIELDS_FIRST_OUTPUT_SAMPLE = SampleWriter.EMPTY;
    private static final long OTHER_OBJECT_WRITTEN_FIELDS_FIRST_OUTPUT_SAMPLE = SampleWriter.EMPTY;
    private static final long TARGET_OBJECT_WRITTEN_FIELDS_SECOND_OUTPUT_SAMPLE = SampleWriter.POSITION;
    private static final long OTHER_OBJECT_WRITTEN_FIELDS_SECOND_OUTPUT_SAMPLE = SampleWriter.EMPTY;
    private int errorsAmount = 0;

    OneMomentAngel(String name, Physics physics, AbstractUniverse universe, Collection<Atom> atoms) {
        super(name, physics, universe, atoms);
    }

    @Override
    public void run() {
        System.out.println(this);
        while (!God.ONE.isGodsWrath()) {
            inputSampleCounter = 0;
            outputSampleCounter = 0;
            try {
                sampleWriter.resetIndexes();
            } catch (Exception e) {
                e.printStackTrace();
            }
            inputSample = sampleWriter.initEmptySample(
                    TARGET_OBJECT_WRITTEN_FIELDS_ZERO_INPUT_SAMPLE
                            + OTHER_OBJECT_WRITTEN_FIELDS_ZERO_INPUT_SAMPLE +
                            (TARGET_OBJECT_WRITTEN_FIELDS_FIRST_INPUT_SAMPLE
                                    + OTHER_OBJECT_WRITTEN_FIELDS_FIRST_INPUT_SAMPLE) * (INPUT_SAMPLES_AMOUNT - 1)
                            + TARGET_OBJECT_WRITTEN_FIELDS_SECOND_INPUT_SAMPLE
                            + OTHER_OBJECT_WRITTEN_FIELDS_SECOND_INPUT_SAMPLE);
//            System.out.println("inputSample = " + inputSample.length);
            outputSample = sampleWriter.initEmptySample(TARGET_OBJECT_WRITTEN_FIELDS_FIRST_OUTPUT_SAMPLE
                    + OTHER_OBJECT_WRITTEN_FIELDS_FIRST_OUTPUT_SAMPLE
                    + TARGET_OBJECT_WRITTEN_FIELDS_SECOND_OUTPUT_SAMPLE
                    + OTHER_OBJECT_WRITTEN_FIELDS_SECOND_OUTPUT_SAMPLE);
//            System.out.println("outputSample = " + outputSample.length);
            if (pip == 2000) {
                pip = 0;
            } else {
                pip += 1000 * PhysicsConfigurations.NewtonPhysicsConfigurations.MOMENT_DURATION;
            }
            long begin = System.currentTimeMillis();
            if (atomsForDeletion != null && atomsForDeletion.size() > 0) {
                ownAtoms.removeAll(atomsForDeletion);
                atomsForDeletion.clear();
            }
            if (atomsForAdding != null && atomsForAdding.size() > 0) {
                ownAtoms.addAll(atomsForAdding);
                atomsForAdding.clear();
            }
            physics.finishWork(this);
            while (!physics.isPrepared(this)) ;
            while (!physics.isPrepared(this)) ;
            while (!physics.isPrepared(this)) ;
            while (!physics.isPrepared(this)) ;
            physics.startWork(this);

            if (!ownAtoms.isEmpty() && (universe.getStage() == UniverseStage.ALIVE ||
                    universe.getStage() == UniverseStage.NEXT_FRAME
            )) {
                int iMax = (int) (1d / PhysicsConfigurations.NewtonPhysicsConfigurations.MOMENT_DURATION);
                // Important: i * PhysicsConfigurations.NewtonPhysicsConfigurations.MOMENT_DURATION * 1000 < 1000
                for (int i = 0; i < iMax
                        ; i++) {
                    try {
                        if (PhysicsConfigurations.NewtonPhysicsConfigurations.IS_GRAVITY_ENABLE) {
                            physics.gravity(allAtoms, ownAtoms);
                        }
                        if (PhysicsConfigurations.NewtonPhysicsConfigurations.IS_ELECTROMAGNETISM_ENABLE) {
                            physics.electromagnetism(allAtoms, ownAtoms);
                        }
                        ownAtoms.forEach(Atom::move);
                        ownAtoms.forEach(Atom::fixEvent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (i % (iMax / (INPUT_SAMPLES_AMOUNT ) + 1) == 0) {
                        try {
                            if (i == 0) {
                                // only target object acceleration
                                sampleWriter.writeSample(TARGET_OBJECT_WRITTEN_FIELDS_ZERO_INPUT_SAMPLE,
                                        OTHER_OBJECT_WRITTEN_FIELDS_ZERO_INPUT_SAMPLE, inputSample);
                            } else {
                                // only target object acceleration
                                sampleWriter.writeSample(TARGET_OBJECT_WRITTEN_FIELDS_FIRST_INPUT_SAMPLE,
                                        OTHER_OBJECT_WRITTEN_FIELDS_FIRST_INPUT_SAMPLE, inputSample);
                                // empty
                                sampleWriter.writeSample(TARGET_OBJECT_WRITTEN_FIELDS_FIRST_OUTPUT_SAMPLE,
                                        OTHER_OBJECT_WRITTEN_FIELDS_FIRST_OUTPUT_SAMPLE, outputSample);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep((long) (PhysicsConfigurations.MOMENT_DURATION * 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    // target object position, acceleration, weight
                    sampleWriter.writeSample(TARGET_OBJECT_WRITTEN_FIELDS_SECOND_INPUT_SAMPLE,
                            OTHER_OBJECT_WRITTEN_FIELDS_SECOND_INPUT_SAMPLE, inputSample);
                    // other objects position and weight
                    sampleWriter.writeSample(TARGET_OBJECT_WRITTEN_FIELDS_SECOND_OUTPUT_SAMPLE,
                            OTHER_OBJECT_WRITTEN_FIELDS_SECOND_OUTPUT_SAMPLE, outputSample);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                System.out.println("inputSample = " + Arrays.toString(inputSample));
                FileWorker.INSTANCE.write(
                        FileWorker.RegisteredFiles.TRAINING_INPUT_DATA_FILE_NAME,
                        inputSample, 1,
                        FileWorker.WritingMode.WITH_DATE);
                FileWorker.INSTANCE.write(
                        FileWorker.RegisteredFiles.TRAINING_OUTPUT_DATA_FILE_NAME,
                        outputSample, 1,
                        FileWorker.WritingMode.WITH_DATE);
                ownAtoms.clear();
            } else if (!ownAtoms.isEmpty() && universe.getStage() == UniverseStage.BACK_FRAME) {
                System.out.println("BACK");
                ownAtoms.forEach(Atom::backForward);
            }

            long remainingTime = (long) (PhysicsConfigurations.DISPLAYING_MOMENT_MIN_DURATION * 1000 - (System.currentTimeMillis() - begin));
            if (remainingTime > 0) {
                try {
                    Thread.sleep(remainingTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.err.println(" === ERRORS AMOUNT ===");
        System.out.println("errorsAmount = " + errorsAmount);
        System.err.println(" =====================");
        System.out.println("!stopped = " + !stopped);
        System.out.println("!God.ONE.isGodsWrath() = " + !God.ONE.isGodsWrath());
        System.out.println(getName() + " disappeared");
    }


    class SampleWriter {
        private static final long EMPTY = 0b0000;
        private static final long POSITION = 0b0001;
        private static final long ACCELERATION = 0b001000;
        private static final long WEIGHT = 0b1000000;
        private static final int DIMENSIONS_AMOUNT = 3;
        private final HashMap<double[], Integer> columnIndexesMap = new HashMap<>();
        private final HashMap<double[], Integer> columnMaxIndexesMap = new HashMap<>();

        void writeSample(long targetObjectSampleBinaryDescriptor, long otherObjectsSampleBinaryDescriptor,
                         double[] store) throws Exception {
            boolean includePositionForTarget = includePosition(targetObjectSampleBinaryDescriptor);
            boolean includeAccelerationForTarget = includeAcceleration(targetObjectSampleBinaryDescriptor);
            boolean includeWeightForTarget = includeWeight(targetObjectSampleBinaryDescriptor);

            boolean includePositionForOther = includePosition(otherObjectsSampleBinaryDescriptor);
            boolean includeAccelerationForOther = includeAcceleration(otherObjectsSampleBinaryDescriptor);
            boolean includeWeightForOther = includeWeight(otherObjectsSampleBinaryDescriptor);
//            if (columnIndex(store) >= store.length) {
//                throw new Exception("Column index out of bounds");
//            }

            for (Atom a : ownAtoms) {
                if (a.isMarkedByGod()) {
                    writeSubSample(a, store,
                            includePositionForTarget, includeAccelerationForTarget, includeWeightForTarget);
                } else {
                    writeSubSample(a, store,
                            includePositionForOther, includeAccelerationForOther, includeWeightForOther);
                }
            }
        }

        private void writeSubSample(Atom a, double[] store,
                                    boolean includePosition, boolean includeAcceleration, boolean includeWeight) {
            if (includePosition) {
                writeMultidimensionalField(a.getPosition(), store);
            }
            if (includeAcceleration) {
                writeMultidimensionalField(a.getAcceleration(), store);
            }
            if (includeWeight) {
                store[columnIndex(store)] = a.getWeight();
                columnIndexIncrement(store);
            }
        }

        private void writeMultidimensionalField(ThreeVector vector, double[] store) {
            store[columnIndex(store)] = vector.x;
            columnIndexIncrement(store);
            if (DIMENSIONS_AMOUNT > 1) {
                store[columnIndex(store)] = vector.y;
                columnIndexIncrement(store);
            }
            if (DIMENSIONS_AMOUNT > 2) {
                store[columnIndex(store)] = vector.z;
                columnIndexIncrement(store);
            }
        }

        private int columnIndex(double[] store) {
            return columnIndexesMap.get(store);
        }

        private void columnIndexIncrement(double[] store) {
            columnIndexesMap.put(store, columnIndexesMap.get(store) + 1);
        }

        private boolean includePosition(long sampleBinaryDescriptor) {
            return (sampleBinaryDescriptor & POSITION) != 0;
        }

        private boolean includeAcceleration(long sampleBinaryDescriptor) {
            return (sampleBinaryDescriptor & ACCELERATION) != 0;
        }

        private boolean includeWeight(long sampleBinaryDescriptor) {
            return (sampleBinaryDescriptor & WEIGHT) != 0;
        }

        private int howManyPosition(long sampleBinaryDescriptor) {
            return (int) (sampleBinaryDescriptor & 0b111);
        }

        private int howManyAcceleration(long sampleBinaryDescriptor) {
            return (int) ((sampleBinaryDescriptor >> 3) & 0b111);
        }

        private int howManyWeight(long sampleBinaryDescriptor) {
            return (int) ((sampleBinaryDescriptor >> 6) & 0b111);
        }

        private double[] initEmptySample(long sampleBinaryDescriptor) {
            int maxIndexForArray = 0;
            maxIndexForArray += howManyPosition(sampleBinaryDescriptor) * DIMENSIONS_AMOUNT;
            maxIndexForArray += howManyAcceleration(sampleBinaryDescriptor) * DIMENSIONS_AMOUNT;
            maxIndexForArray += howManyWeight(sampleBinaryDescriptor);
            double[] array = new double[maxIndexForArray];
            columnIndexesMap.put(array, 0);
            columnMaxIndexesMap.put(array, maxIndexForArray);
            return array;
        }

        private void resetIndexes() throws Exception {
            for (Map.Entry<double[], Integer> e : columnIndexesMap.<double[], Integer>entrySet()) {
                double[] key = e.getKey();
                Integer value = e.getValue();
                Integer maxIndex = columnMaxIndexesMap.get(key);
                if (0 < value && value < maxIndex) {
                    System.err.println("Current index: " + value + ". Max index: " + maxIndex);
                    errorsAmount++;
//                    throw new Exception("Current index: " + value + ". Max index: " + maxIndex);
                }
                columnIndexesMap.put(key, 0);
            }
        }
    }
}
