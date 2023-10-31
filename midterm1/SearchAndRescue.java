import java.awt.*;
import java.util.ArrayList;

public class SearchAndRescue
{
    protected GridSquare[][] grid;
    protected int width, height;

    private class GridSquare
    {
        private int x,y;
        private double probability;
        public GridSquare(int x, int y, double prob)
        {
            this.x = x;
            this.y = y;
            this.probability = prob;
        }
    }

    public SearchAndRescue(int m, int n)
    {
        grid = new GridSquare[m][n];
        for(int x = 0; x<m; x++)
            for(int y = 0; y<n; y++)
                grid[x][y] = new GridSquare(x, y, 1.0/(m*n));
        width = m;
        height = n;
    }

    public void setGridProbability(int x, int y, Double probability) throws Exception
    {
        if(x>width||x<0||y>height||y<0) throw new Exception();
        grid[x][y].probability = probability;
    }

    public ArrayList<Point> mostLikelyPoints()
    {
        ArrayList<Point> points = new ArrayList<Point>();
        double heighestProb = 0;
        for(int x = 0; x<height; x++)
        {
            for (int y = 0; y < width; y++)
            {
                if (grid[x][y].probability > heighestProb)
                {
                    points = new ArrayList<Point>();
                    points.add(new Point(x, y));
                    heighestProb = grid[x][y].probability;
                }
                else if (grid[x][y].probability == heighestProb)
                {
                    points.add(new Point(x, y));
                }
            }
        }
        return points;
    }

    public String toString()
    {
        String result = "\t";
        for(int x = 0; x<width; x++)
        {
            for(int y = 0; y<height; y++)
            {
                if(y==0) result += "\t" + x; //tabs not working for some reason
                else if(x==0) result+=y;
                else
                {
                    result += "\t" + grid[x][y].probability;
                }
            }

            result += "\n";
            if(x==0) result+=0;
        }
        return result;
    }
}
