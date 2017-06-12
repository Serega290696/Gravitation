package being.mathematics;

import java.io.Serializable;

public class ThreeVector implements Cloneable, Serializable {
    public double x;
    public double y;
    public double z;

    public ThreeVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ThreeVector(double radian, double module) {
        module = Math.abs(module);
        this.x = Math.cos(radian) * module;
        this.y = Math.sin(radian) * module;
        this.z = 0;
    }

    public ThreeVector divide(double number) {
        return new ThreeVector(x / number, y / number, z / number);
    }

    public ThreeVector multiply(double number) {
        return new ThreeVector(x * number, y * number, z * number);
    }

    public ThreeVector minus(ThreeVector subtrahend) {
        return new ThreeVector(x - subtrahend.x, y - subtrahend.y, z - subtrahend.z);
    }

    public ThreeVector plus(ThreeVector term) {
        return new ThreeVector(x + term.x, y + term.y, z + term.z);
    }

    public void plusAndUpdate(ThreeVector term) {
        x = x + term.x;
        y = y + term.y;
        z = z + term.z;
    }

    public double module() {
        return Math.abs(Math.pow(x * x + y * y + z * z, 0.5));
    }

    @Override
    public String toString() {
        return "Vector(" +
                MathUtils.round(x, 10) +
                ", " + MathUtils.round(y, 10) +
                ", " + MathUtils.round(z, 10) +
                ')';
    }

    @Override
    public ThreeVector clone() {
//    System.err.println("[WARN] in clone()");
        return new ThreeVector(x, y, z);
    }

    public ThreeVector normalize() {
        return new ThreeVector(x / this.module(), y / this.module(), z / this.module());
    }

    public double angle() {
        double angle = 0;
        ThreeVector norma = this.normalize();
        double innerAngle = Math.atan(Math.abs(norma.y / norma.x));
        if (x >= 0 && y >= 0) {
            angle = innerAngle;
        } else if (x >= 0 && y < 0) {
            angle = 2 * Math.PI - innerAngle;
        } else if (x < 0 && y >= 0) {
            angle = Math.PI / 2 - innerAngle + Math.PI / 2;
        } else if (x < 0 && y < 0) {
            angle = innerAngle + Math.PI;
        } else {
            System.err.println("Wrong angle value: " + innerAngle);
        }
        return angle;
    }

    public ThreeVector projectOn(ThreeVector vector) {
        double angle = this.angle();
        double angle2 = vector.angle();
        double deltaAngle = angle - angle2;
        if (Math.abs(deltaAngle) <= Math.PI / 2) {
            return new ThreeVector(vector.angle(), Math.abs(Math.cos(deltaAngle) * this.module()));
        } else {
            return new ThreeVector(vector.angle() + Math.PI, Math.abs(Math.cos(deltaAngle) * this.module()));
        }
    }

    public ThreeVector projectRestOn(ThreeVector vector) {
        double angle = this.angle();
        double angle2 = vector.angle();
        double deltaAngle = (angle - angle2) % (2 * Math.PI);
        double resultAngle = vector.angle() + Math.PI / 2;
        if (deltaAngle > Math.PI || (deltaAngle < 0 && deltaAngle > -Math.PI)) {
            resultAngle *= -1;
        }
        return new ThreeVector(resultAngle, Math.abs(Math.sin(deltaAngle) * this.module()));
    }
}
