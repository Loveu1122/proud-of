import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawableShape;
import org.opensourcephysics.display.DrawableTextLine;
import org.opensourcephysics.display.Trail;
import org.opensourcephysics.frames.PlotFrame;
import java.awt.Color;
import org.dalton.polyfun.Polynomial;
import java.util.Scanner;





public class RiemannSumsApp extends AbstractSimulation {
PlotFrame plotFrame = new PlotFrame("x", "f(x)", "Riemann Sums");
String rule;
boolean plotted = false;






/**
 * Adds options to the Control Panel.
 */
@Override
public void reset() {
    
    // Create a new plot frame and allowing the user to configure it

    // Blocks will decide the width of each individual rectangle --> f(15) --> 15 blocks with a individual width of 1
    control.setValue("How many blocks?", 1);

    // Value plugged into f(x) --> f(x) = 3x^2 --> f(15) = 3(15)^2
    control.setValue("What # should be plugged into f(x)?", 15);

    //minimum range for the x-axis
    control.setValue("Minimum X-value",0);

    //minimum range for the y-axis
    control.setValue("Minimum Y-value", 0);


    //coefficients for the polynomial
    control.setValue("Coeff. x^2", 3);
    control.setValue("Coeff. x", 2);
    control.setValue("Constant", 1);

    //rules
    control.setValue("Rule (Mid, Left, Right, Trap)", "Mid");
}


/**
 * Gets information from the Control Panel and configures the plot frame.
 */
@Override
public void initialize() {

    // redo the plotFrame
    plotFrame.clearData();
    plotFrame.clearDrawables();
    plotFrame.setConnected(true);

// basic rulers to decide which rule the user wants to use
String rulers = control.getString("Rule (Mid, Left, Right, Trap)");




//if statements that decide which rule is at play
if ("Left".equals(rulers)){
    plotLeftRule();
} else if ("Right".equals(rulers)){
    plotRightRule();
} else if ("Mid".equals(rulers)){
    plotMidRule();
} else if ("Trap".equals(rulers)){
    plotTrapRule();
} else if ("Random".equals(rulers)){
    plotRandomRule();
}


//comparing area accuracy
compareAccuracy();



    // OPTION 1: Append connected points1
    plotFrame.setLineColor(0, Color.BLUE);   // optional set line color
    plotFrame.setConnected(true); // connect the points








    plotFrame.setDefaultCloseOperation(3); // Make it so x'ing out of the graph stops the program.
    plotFrame.setVisible(true); // Required to show plot frame.
}



@Override
protected void doStep() {

}



//creating the midpoint rule
private void plotMidRule() {

    //getting coefficents from user input
    double a = control.getDouble("Coeff. x^2");
    double b = control.getDouble("Coeff. x");
    double c = control.getDouble("Constant");

    // Create the polynomial function
    Polynomial fx = new Polynomial(new double[] {c,b,a});

    //integral calculation --> How integration works simply put
    Polynomial integralFx = new Polynomial(new double[] {0,c,b/2,a/3}); //

    //printing fx function
    System.out.println("f(x) = " +fx);

    //f(x) plug in
    double x = control.getDouble("What # should be plugged into f(x)?");

    //x-value range for minimum value
    double xMin = control.getDouble("Minimum X-value");


    //y-value range for max and min values
    double yMin = control.getDouble("Minimum Y-value");

    //amount of blocks --> width from above
    double blockAmount = control.getDouble("How many blocks?");

    //original area that will configure later on
    double areaMidRule = 0;

    //width that changes over time
    double width = 1/blockAmount;

    //integral calculations
    double ActualArea = integralFx.eval(x) - integralFx.eval(xMin);


    /*basic for statement that replays the function and creates the rectangles
    - i+= (width) --> stating that the width of the blocks decides on the user input
    - i = yMin so the user can configure the y-axis and how f(x) is plotted
    */


    for (double i = xMin; i < x; i+= (width)){

        //ploting the function
        plotFrame.append(0,i,fx.eval(i));

        //printing the function 
        System.out.println("f(" +i +") = " +fx.eval(i));

        //setting the x and y ranges that depend on the for statement and user input
        plotFrame.setPreferredMinMax(xMin,i, yMin, fx.eval(i));
        DrawableShape rectangle = DrawableShape.createRectangle(i, (fx.eval(i)/2), width ,fx.eval(i));

        //ploting the rectangle
        plotFrame.addDrawable(rectangle);

        /*area formula
            - Explination: The midplot rule display several boxes with their halfpoint intersecting with the function. Now, when calculating the area of the 
            midplot rule, the width is simply the width of the rectangle, and we multiply that by the height of the individual rectangle. We add the width and
            divide by two because we’re shifting from the left endpoint to the center of each subinterval.
        */ 

        areaMidRule += width * fx.eval(i + width / 2);

    }

    //labeling the actual area, estimated area, rule, and difference on the graph
    DrawableTextLine areaLabelMidRule = new DrawableTextLine("act. Area = "+ActualArea+" –– est. Area = "+areaMidRule +" –– Rule = Mid. Rule –– Difference = " + (ActualArea - areaMidRule), x/3, fx.eval(x)/1.5);
    plotFrame.addDrawable(areaLabelMidRule);


}

//creating the left rule
private void plotLeftRule() {

    //getting coefficents
    double a = control.getDouble("Coeff. x^2");
    double b = control.getDouble("Coeff. x");
    double c = control.getDouble("Constant");


    // get x and block amount
    double x = control.getDouble("What # should be plugged into f(x)?");

    //x-value range minimum
    double xMin = control.getDouble("Minimum X-value");

    //y-value range minimum
    double yMin = control.getDouble("Minimum X-value");

    //block amount
    double blockAmount = control.getDouble("How many blocks?");

    //polynomial function
    Polynomial fx = new Polynomial(new double[] {c,b,a});

    //integral calculation
    Polynomial integralFx = new Polynomial(new double[] {0,c,b/2,a/3}); //
    System.out.println("f(x) = " +fx);


    //original area
    double areaLeftRule = 0;

    //width
    double width = 1/blockAmount;

    //integral calculations
    double ActualArea = integralFx.eval(x) - integralFx.eval(xMin);


    for (double i = xMin; i < x; i+= (width)){

        plotFrame.append(0,i,fx.eval(i));
        System.out.println("f(" +i +") = " +fx.eval(i));
        plotFrame.setPreferredMinMax(xMin,i, yMin, fx.eval(i)); // x and y ranges
        DrawableShape rectangle = DrawableShape.createRectangle((i+.5), (fx.eval(i)/2), width,fx.eval(i));
        plotFrame.addDrawable(rectangle);

            /*area for the left rule
                - Explination: The left rule underestimate the area, and the equation is simple: based * height
            */

            areaLeftRule += (width*fx.eval(i));
    }

    //draw a Line with actualArea, estimated area, rule, and difference
    DrawableTextLine areaLabelLeft = new DrawableTextLine("act. Area = "+ActualArea+" –– est. Area = "+areaLeftRule+" –– Rule = Left Rule –– Difference = " + (ActualArea - areaLeftRule), x/3, fx.eval(x)/1.5);
    plotFrame.addDrawable(areaLabelLeft);

}

//creating the right rule
private void plotRightRule() {

    //getting coefficents
    double a = control.getDouble("Coeff. x^2");
    double b = control.getDouble("Coeff. x");
    double c = control.getDouble("Constant");
    Polynomial fx = new Polynomial(new double[] {c,b,a});

    //integral calculation
    Polynomial integralFx = new Polynomial(new double[] {0,c,b/2,a/3}); //
    System.out.println("f(x) = " +fx);

    //x range
    double x = control.getDouble("What # should be plugged into f(x)?");

    //x-value range
    double xMin = control.getDouble("Minimum X-value");

    //y-value range
    double yMin = control.getDouble("Minimum Y-value");

    //amount of blocks
    double blockAmount = control.getDouble("How many blocks?");

    //original area
    double areaRightRule = 0;

    //width
    double width = 1/blockAmount;

    //integral calculations
    double ActualArea = integralFx.eval(x) - integralFx.eval(xMin);

    for (double i = xMin; i < x; i+= (width)){


        plotFrame.append(0,i,fx.eval(i));
        System.out.println("f(" +i +") = " +fx.eval(i));
        plotFrame.setPreferredMinMax(xMin,i, yMin, fx.eval(i)); // x and y ranges
        DrawableShape rectangle = DrawableShape.createRectangle((i-.5), (fx.eval(i)/2), width,fx.eval(i));
        plotFrame.addDrawable(rectangle);

        //area is simple again --> width * (height * width)
        areaRightRule += (width*fx.eval(i + width));

    }

    // simple text label for actual area, estimated area, rule, and difference
    DrawableTextLine areaLabelRightRule = new DrawableTextLine("act. Area = "+ActualArea+" –– est. Area = "+areaRightRule +" –– Rule = Right Rule –– Difference = " + (ActualArea - areaRightRule), x/3, fx.eval(x)/1.5);
    plotFrame.addDrawable(areaLabelRightRule);


}

//creating the trapezoid rule
private void plotTrapRule() {

   //getting coefficents
   double a = control.getDouble("Coeff. x^2");
   double b = control.getDouble("Coeff. x");
   double c = control.getDouble("Constant");
   Polynomial fx = new Polynomial(new double[] {c,b,a});

   //integral calculation
   Polynomial integralFx = new Polynomial(new double[] {0,c,b/2,a/3}); //
   System.out.println("f(x) = " +fx);

   //x range
   double x = control.getDouble("What # should be plugged into f(x)?");

    //x-value range
    double xMin = control.getDouble("Minimum X-value");

    //Y-value range
    double yMin = control.getDouble("Minimum X-value");

   //amount of blocks
   double blockAmount = control.getDouble("How many blocks?");

   //original area
   double areaTrapRule = 0;

   //width
   double width = 1/blockAmount;

   //integral calculations
   double ActualArea = integralFx.eval(x) - integralFx.eval(xMin);

    for (double i = xMin; i < x; i+= (width)){
    
        plotFrame.append(0,i,fx.eval(i));
        System.out.println("f(" +i +") = " +fx.eval(i));
        plotFrame.setPreferredMinMax(xMin,i, yMin, fx.eval(i)); // x and y ranges

        /*trapezoid rule --> creating a new trail that all connects path
        - first trail is at i, and 0
        - second trail is at i, and the new y-value
        - third trail is at i + width...
        - fourth trail is at i + width, and 0
        - fifth trail is at i, and 0

        you are simply making a trapezoid with 5 points that are all connected
        */

        Trail trail = new Trail();
        trail.setConnected(true);
        trail.addPoint(i,0);
        trail.addPoint(i, fx.eval(i));
        trail.addPoint(i+width, fx.eval(i+width));
        trail.addPoint(i+width, 0);
        trail.addPoint(i,0);

        //draw trail
        plotFrame.addDrawable(trail);

        //area calculation
        areaTrapRule +=  (fx.eval(i) + fx.eval(i+width))/2 * width;
    }

// drawing the actualArea, estimated area, rule, and difference
DrawableTextLine areaLabelTrapRule = new DrawableTextLine("act. Area = "+ActualArea+" –– est. Area = "+areaTrapRule +" –– Rule = Trapezoid Rule –– Difference = " + (ActualArea - areaTrapRule), x/3, fx.eval(x)/1.5);
plotFrame.addDrawable(areaLabelTrapRule);

}

// Adding my new rule --> Random Rule
private void plotRandomRule() {

   //getting coefficents
   double a = control.getDouble("Coeff. x^2");
   double b = control.getDouble("Coeff. x");
   double c = control.getDouble("Constant");
   Polynomial fx = new Polynomial(new double[] {c,b,a});

   //integral calculation
   Polynomial integralFx = new Polynomial(new double[] {0,c,b/2,a/3}); //
   System.out.println("f(x) = " +fx);

   //x range
   double x = control.getDouble("What # should be plugged into f(x)?");

    //x-value range
    double xMin = control.getDouble("Minimum X-value");

    //x-value range
    double yMin = control.getDouble("Minimum X-value");

   //amount of blocks
   double blockAmount = control.getDouble("How many blocks?");

   //original area
   double areaRandomRule = 0;

   //width
   double width = 1/blockAmount;

   //integral calculations
   double ActualArea = integralFx.eval(x) - integralFx.eval(xMin);


    for (double i = xMin; i < x; i+= (width)){
    
        //random number maker
        double randomX = i + Math.random() * width;

        //finding the random height
        double height = fx.eval(randomX);

        plotFrame.append(0, i, fx.eval(i));
        System.out.println("f(" + randomX + ") = " + height);
        plotFrame.setPreferredMinMax(xMin, i, yMin, fx.eval(i)); // x and y ranges
        DrawableShape rectangle = DrawableShape.createRectangle(randomX, height/2, width, height);

        //simple area
        areaRandomRule += width * height;
       
    }

// drawing the random rule --> actualArea, rule, and difference
DrawableTextLine areaLabelRandomRule = new DrawableTextLine("act. Area = "+ActualArea+" –– est. Area = "+areaRandomRule +" –– Rule = Trapezoid Rule –– Difference = " + (ActualArea - areaRandomRule), x/3, fx.eval(x)/1.5);
plotFrame.addDrawable(areaLabelRandomRule);

}

//this entire piece of code below is to compare accuracy

//this one is for the midpoint rule
private double calculateMidArea(Polynomial fx, double x, double blockAmount) {

    double area = 0;
    double width = 1/blockAmount;

    for (double i = 0; i < x; i += width) {

        //midpoint rule area
        area += width * fx.eval(i + width/2);
    }

    //returning area
    return area;
}

//left area calculations
private double calculateLeftArea(Polynomial fx, double x, double blockAmount) {
    double area = 0;
    double width = 1/blockAmount;
    
    for (double i = 0; i < x; i += width) {

        //left rule area
        area += (width*fx.eval(i));
    }
    return area;
}


//right area calculation
private double calculateRightArea(Polynomial fx, double x, double blockAmount) {
    double area = 0;
    double width = 1/blockAmount;
    
    for (double i = 0; i < x; i += width) {

        //right rule area
        area += (width*fx.eval(i + width));
    }
    return area;
}

//trapezoid area calculation
private double calculateTrapArea(Polynomial fx, double x, double blockAmount) {
    double area = 0;
    double width = 1/blockAmount;
    
    for (double i = 0; i < x; i += width) {

        //trapezoid area
        area += (fx.eval(i) + fx.eval(i+width))/2 * width;
    }
    return area;
}

//random area calculation
private double calculateRandomArea (Polynomial fx, double x, double blockAmount){
    double area = 0;
    double width = 1/blockAmount;

    for (double i = 0; i < x; i += width) {

        //randomX maker again
        double randomX = i + Math.random() * width;

        //random rule area
        double height = fx.eval(randomX);

        area += width * height;
    }
    return area;

}



public void compareAccuracy() {

    // Get coefficients from the control panel
    double a = control.getDouble("Coeff. x^2");
    double b = control.getDouble("Coeff. x");
    double c = control.getDouble("Constant");



    // Create the polynomial function
    Polynomial fx = new Polynomial(new double[] {c,b,a});
    Polynomial integralFx = new Polynomial(new double[] {0,c,b/2,a/3});
    
    //range of x
    double x = control.getDouble("What # should be plugged into f(x)?");

    //block amount
    double blockAmount = control.getDouble("How many blocks?");

    //x-value range
    double xMin = control.getDouble("Minimum X-value");

    //integral calculation
    double actualArea = integralFx.eval(x) - integralFx.eval(xMin);

    // Calculate areas using different methods
    double midArea = calculateMidArea(fx, x, blockAmount);
    double leftArea = calculateLeftArea(fx, x, blockAmount);
    double rightArea = calculateRightArea(fx, x, blockAmount);
    double trapArea = calculateTrapArea(fx, x, blockAmount);
    double randomArea = calculateRandomArea(fx, x, blockAmount);


    //calculate differences --> Math.abs = absolute value of the difference - very accurate
    double midDiff = Math.abs(actualArea - midArea);
    double leftDiff = Math.abs(actualArea - leftArea);
    double rightDiff = Math.abs(actualArea - rightArea);
    double trapDiff = Math.abs(actualArea - trapArea);
    double randomDiff = Math.abs(actualArea - randomArea);

    //Math.min(a, b) simply returns the lesser of the two values a and b.
    double minDiff = Math.min(Math.min(midDiff, leftDiff), Math.min(rightDiff, trapDiff));
    
    //outprinting the results
    System.out.println("––––––––––––––");
    System.out.println("Actual Area: " + actualArea);
    System.out.println("––––––––––––––");
    System.out.println("Mid Rule Area: " + midArea + " (error: " + Math.abs(actualArea - midArea) + ")");
    System.out.println("Left Rule Area: " + leftArea + " (error: " + Math.abs(actualArea - leftArea) + ")");
    System.out.println("Right Rule Area: " + rightArea + " (error: " + Math.abs(actualArea - rightArea) + ")");
    System.out.println("Trap Rule Area: " + trapArea + " (error: " + Math.abs(actualArea - trapArea) + ")");
    System.out.println("Random Rule Area: " + randomArea + " (error: " + Math.abs(actualArea - randomArea) + ")");
    System.out.println("––––––––––––––");

    //most accurate result 
    System.out.println("Most accurate method:");
    if (minDiff == midDiff) {
        System.out.println("Midpoint Rule is the most accurate, with error of " + midDiff);
    } else if (minDiff == leftDiff) {
        System.out.println("Left Rule is the most accurate, with error of " + leftDiff);
    } else if (minDiff == rightDiff) {
        System.out.println("Right Rule is the most accurate, with error of " + rightDiff);
    } else if (minDiff == trapDiff) { 
        System.out.println("Trapezoid Rule is the most accurate, with error of " + trapDiff);
    } else {
        System.out.println("Random Rule is the most accurate, with error of " + randomDiff);
    }

}








/**
 * Required main method, runs the simulation.
 * @param args
 */
public static void main(String[] args) {
    SimulationControl.createApp(new RiemannSumsApp());


}


public PlotFrame getPlotFrame() {
    return plotFrame;
}


public void setPlotFrame(PlotFrame plotFrame) {
    this.plotFrame = plotFrame;
}


public String getRule() {
    return rule;
}


public void setRule(String rule) {
    this.rule = rule;
}


public boolean isPlotted() {
    return plotted;
}


public void setPlotted(boolean plotted) {
    this.plotted = plotted;
}
}
