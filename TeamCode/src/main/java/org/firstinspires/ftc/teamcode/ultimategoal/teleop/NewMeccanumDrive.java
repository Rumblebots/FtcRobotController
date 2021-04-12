package org.firstinspires.ftc.teamcode.ultimategoal.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import org.firstinspires.ftc.teamcode.ultimategoal.ControllerImpl;
import org.firstinspires.ftc.teamcode.ultimategoal.pathfinder.PathfinderConstants;
import org.rx.core.Ticker;
import org.rx.core.input.controller.AbstractXbc;
import org.rx.core.util.AutoButton;
import org.rx.core.util.AutoButtonToggle;
import org.rx.core.util.ButtonSwitcher;

import java.util.HashMap;

@TeleOp(name = "New Meccanum", group = "Test")
public class NewMeccanumDrive extends OpMode {
    private AbstractXbc controller1 = new ControllerImpl(gamepad1);
    private AbstractXbc controller2 = new ControllerImpl(gamepad2);
    AutoButton speedF = new AutoButton(() -> controller1.triggerRight() > 0);
    AutoButton speedL = new AutoButton(() -> controller1.triggerLeft() > 0);
    AutoButton speedN = new AutoButton(() -> controller1.triggerRight() == 0 && controller1.triggerLeft() == 0);
    ButtonSwitcher<Double> speed = new ButtonSwitcher<>(new HashMap<>() {{
        put(speedF, 1.0);
        put(speedL, 0.25);
        put(speedN, 0.5);
    }});
    AutoButton wobbleU = controller2.dpadUp;
    AutoButton wobbleD = controller2.dpadDown;
    AutoButton pusher = controller2.bumperRight;
    AutoButton intakeO = new AutoButton(this::getIntakeO);
    AutoButton intakeI = new AutoButton(this::getIntakeI);
    AutoButtonToggle shooterToggle = new AutoButtonToggle(controller2::a);
    AutoButtonToggle wobbleToggle = new AutoButtonToggle(controller2::x);
    AutoButton gp1a = controller1.a;
    AutoButton gp1b = controller1.b;
    AutoButton gp1x = controller1.x;
    AutoButton gp1y = controller1.y;
    ButtonSwitcher<HeadingPoint> targets = new ButtonSwitcher<>(new HashMap<>() {{
        put(gp1a, new HeadingPoint(0, 0, 0));
        put(gp1b, new HeadingPoint(0, 0, 0));
        put(gp1x, new HeadingPoint(0, 0, 0));
        put(gp1y, new HeadingPoint(0, 0, 0));
    }});
    Pathfinder pathfinder;
    Ticker controlTicker = new Ticker(
            controller1,
            controller2,
            speedF,
            speedL,
            speedN,
            speed,
            intakeO,
            intakeI,
            shooterToggle,
            wobbleToggle,
            targets,
            pathfinder::tick
    );
    DcMotor fr, fl, br, bl, flywheel1, flywheel2, intake;
    CRServo lowerIntakeServo, upperIntakeServo;
    Servo intakeMover, loaderServo, pusherServo, wobbleArm, wobbleDropper;

    private boolean getIntakeI() {
        return gamepad2.left_trigger > gamepad2.right_trigger &&
                gamepad2.left_trigger > 0.3;
    }

    private boolean getIntakeO() {
        return gamepad2.right_trigger > gamepad2.left_trigger &&
                gamepad2.right_trigger > 0.3;
    }

    private DcMotor getMotor(String name) {
        return hardwareMap.get(DcMotor.class, name);
    }

    private CRServo getCR(String name) {
        return hardwareMap.get(CRServo.class, name);
    }

    private Servo getServo(String name) {
        return hardwareMap.get(Servo.class, name);
    }

    @Override
    public void init() {
        fr = getMotor("frontRight");
        fl = getMotor("frontLeft");
        br = getMotor("backRight");
        bl = getMotor("backLeft");

        flywheel1 = getMotor("flywheel1");
        flywheel2 = getMotor("flywheel2");

        intake = getMotor("intakeMotor");
        lowerIntakeServo = getCR("intakeServo");
        upperIntakeServo = getCR("upperIntakeServo");
        intakeMover = getServo("intakeMover");

        loaderServo = getServo("loader");
        pusherServo = getServo("pusher");
        wobbleArm = getServo("wobbleArm");
        wobbleDropper = getServo("wobbleDropper");

        PathfinderConstants.initializeMotors(hardwareMap);
        PathfinderConstants.initializePathfinder(() -> false);
        pathfinder = PathfinderConstants.getPathfinder();
        PathfinderConstants.getChassisTracker().setOffset(Point.ZERO);
    }

    @Override
    public void loop() {
        // tick everything in the control ticker
        // also ticks pathfinder
        controlTicker.tick();

        // drive the robot!! woot woot!
        double multiplier = speed.getState();
        double _fr = -controller1.leftY() - controller1.rightX() - controller1.leftX();
        double _br = -controller1.leftY() - controller1.rightX() + controller1.leftX();
        double _fl = +controller1.leftY() - controller1.rightX() + controller1.leftX();
        double _bl = +controller1.leftY() - controller1.rightX() - controller1.leftX();
        fr.setPower(_fr * multiplier);
        fl.setPower(_fl * multiplier);
        br.setPower(_br * multiplier);
        bl.setPower(_bl * multiplier);

        // if any of the A, B, X, Y buttons are pressed we need to queue a path
        if (gp1a.isPressed() ||
                gp1b.isPressed() ||
                gp1x.isPressed() ||
                gp1y.isPressed()) {
            pathfinder.followPath(new DynamicArray<>(
                    HeadingPoint.pointOrIfNullZero(pathfinder.getPosition()),
                    targets.getState()
            ));
        }

        // if RB is pressed stop pathfinder from following a path
        // sort of like a manual override
        if (controller1.bumperRight()) {
            pathfinder.getManager().getExecutor().clear();
        }

        //  dpad up  = wobble goal to 0.00
        // dpad down = wobble goal to 0.94
        if (wobbleU.isPressed()) {
            wobbleArm.setPosition(0.0);
        } else if (wobbleD.isPressed()) {
            wobbleArm.setPosition(0.94);
        }

        // if pusher toggle is active, set pos to 0.65,
        // otherwise 1
        if (pusher.isActive()) {
            pusherServo.setPosition(0.65);
        } else {
            pusherServo.setPosition(1);
        }

        if (intakeO.isPressed()) {
            // intake out
            intake.setPower(1.0);
            lowerIntakeServo.setPower(0.8);
            upperIntakeServo.setPower(0.8);
        } else if (intakeI.isPressed()) {
            // intake in
            intake.setPower(-1.0);
            lowerIntakeServo.setPower(-0.8);
            upperIntakeServo.setPower(-0.8);

            loaderServo.setPosition(1.0);
        } else {
            // neither out nor in
            intake.setPower(0.0);
            lowerIntakeServo.setPower(0.0);
            upperIntakeServo.setPower(0.0);

            loaderServo.setPosition((180D - 36D) / 180D);
        }

        // wobble toggle stuff
        if (wobbleToggle.state()) {
            wobbleDropper.setPosition(1.0);
        } else {
            wobbleDropper.setPosition(0.0);
        }

        // turn the shooter on or off
        if (shooterToggle.state()) {
            flywheel1.setPower(1.0);
            flywheel2.setPower(1.0);
        } else {
            flywheel1.setPower(0.0);
            flywheel2.setPower(0.0);
        }
    }
}
