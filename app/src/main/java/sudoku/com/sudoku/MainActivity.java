package sudoku.com.sudoku;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.sudoku.R;

public class MainActivity extends AppCompatActivity {

    List< List< List<Integer> > > arr=new ArrayList<List <List <Integer>>>();
    int bf=0;
    int test=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDesign();
        createArray(arr);

        final Button solveButton=findViewById(R.id.SolveButton);
        solveButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {

                fillArray();
                startSolving();
            }
        });

    }

    void startSolving()
    {
        Button solveButton=findViewById(R.id.SolveButton);

        if(solve())
        {
            fillSudoku();
            solveButton.setText(this.getString(R.string.solved));

        }
        else
        {
            fillSudoku();
            solveButton.setText(this.getString(R.string.cannot_solved));
        }
        bf=0;
    }
    void createDesign()
    {
        TableLayout tl=findViewById(R.id.sudoku);
        tl.removeAllViews();
        int i,j;
        for(i=0; i<9; i++)
        {
            TableRow tr = (TableRow)getLayoutInflater().inflate(R.layout.row,tl, false);

            for(j=0; j<9; j++)
            {
                EditText child = (EditText)getLayoutInflater().inflate(R.layout.item, tr,false);
                tr.addView(child);
                final EditText et=(EditText)tr.getChildAt(j);
                et.setText(testCase());
                
                et.setOnClickListener(new OnClickListener(){

                    @Override
                    public void onClick(View arg0) {

                        et.setText("");
                    }

                });

            }
            tl.addView(tr);
        }
    }


    String testCase()
    {
        String[] s=new String[9];
        s[0]="0 0 0 0 0 0 8 0 0";
        s[1]="0 7 0 0 0 3 0 0 9";
        s[2]="9 8 3 4 0 0 0 0 0";
        s[3]="0 3 8 1 7 0 0 0 0";
        s[4]="0 5 0 0 0 0 0 2 0";
        s[5]="0 0 0 0 2 6 3 7 0";
        s[6]="0 0 0 0 0 9 7 8 4";
        s[7]="7 0 0 2 0 0 0 9 0";
        s[8]="0 0 4 0 0 0 0 0 0";

        test++;
        //return "0";  /*if you dont want to fill the data with these test cases you can return 0*/
        return String.valueOf(s[(test/9)].charAt(test%9));

    }
    void fillArray()
    {
        int i,j,k;
        TableLayout tl=findViewById(R.id.sudoku);
        TableRow tr;
        EditText et;
        String s;
        for(i=0; i<9; i++)
        {
            tr=(TableRow)tl.getChildAt(i);
            for(j=0; j<9; j++)
            {
                et=(EditText)tr.getChildAt(j);
                s=et.getText().toString().replace(" ", "");
                if(s.contentEquals(" ") || s.contentEquals("") ||s.contentEquals("0"))
                {
                    for(k=0; k<10; k++)
                    {
                        arr.get(i).get(j).add(k);
                    }
                }
                else
                    arr.get(i).get(j).add(Integer.parseInt(s));
            }
        }
    }

    void fillSudoku()
    {
        int i,j;
        TableLayout tl=findViewById(R.id.sudoku);
        TableRow tr;
        EditText et;
        for(i=0; i<9; i++)
        {
            tr=(TableRow)tl.getChildAt(i);
            for(j=0; j<9; j++)
            {
                et=(EditText)tr.getChildAt(j);
                et.setText(String.valueOf(arr.get(i).get(j).get(0)));
            }
        }
    }

    void createArray(List<List<List<Integer>>> a)
    {
        int i,j;
        for(i=0; i<9; i++)
        {
            List<List<Integer>> tmp1=new ArrayList<List<Integer> >();
            for(j=0; j<9; j++)
            {
                List<Integer> tmp2=new ArrayList<Integer>();
                tmp1.add(tmp2);
            }
            a.add(tmp1);
        }


    }

    boolean update(int x, int y)
    {
        int i,j,n;
        int tmp;
        n=arr.get(x).get(y).get(0);
        for(i=0; i<9; i++)
        {
            //same row
            tmp=arr.get(x).get(i).get(0);
            if(tmp==0)
            {
                arr.get(x).get(i).remove(Integer.valueOf(n));
                if(arr.get(x).get(i).size()==2)
                {
                    if(!allowed(x,i,arr.get(x).get(i).get(1)))
                        return false;
                    arr.get(x).get(i).remove(0);
                    update(x,i);
                }
                else if(arr.get(x).get(i).size()==1)
                    return false;
            }
            //same column
            tmp=arr.get(i).get(y).get(0);
            if(tmp==0)
            {
                arr.get(i).get(y).remove(Integer.valueOf(n));
                if(arr.get(i).get(y).size()==2)
                {
                    if(!allowed(i,y,arr.get(i).get(y).get(1)))
                        return false;
                    arr.get(i).get(y).remove(0);
                    update(i,y);
                }
                else if(arr.get(i).get(y).size()==1)
                    return false;
            }
        }

        //same block
        for(i=x-(x%3); i<x-(x%3)+3 ; i++)
        {
            for(j=y-(y%3); j<y-(y%3)+3 ; j++)
            {
                tmp=arr.get(i).get(j).get(0);
                if(tmp==0)
                {
                    arr.get(i).get(j).remove(Integer.valueOf(n));
                    if(arr.get(i).get(j).size()==2)
                    {
                        if(!allowed(i,j,arr.get(i).get(j).get(1)))
                            return false;
                        arr.get(i).get(j).remove(0);
                        update(i,j);
                    }
                    else if(arr.get(i).get(j).size()==1)
                        return false;
                }
            }
        }
        return true;
    }

    void copyState(List< List< List <Integer > > > dest, List< List< List <Integer > > > src)
    {
        int i,j,k;
        for(i=0; i<9; i++)
        {
            for(j=0; j<9; j++)
            {
                dest.get(i).get(j).clear();
                for(k=0; k<src.get(i).get(j).size(); k++)
                {
                    dest.get(i).get(j).add(src.get(i).get(j).get(k));
                }
            }
        }
    }

    boolean solve()
    {
        int i,j;
        for(i=0; i<9; i++)
        {
            for(j=0; j<9; j++)
            {
                if(arr.get(i).get(j).get(0)!=0)
                {
                    update(i,j);
                }
            }
        }
        if(complete())
            return true;
        return bruteForce();
    }

    boolean complete()
    {
        int i,j;
        for(i=0; i<9; i++)
        {
            for(j=0; j<9; j++)
            {
                if(arr.get(i).get(j).get(0)==0)
                    return false;
            }
        }
        return valid();
    }

    boolean allowed(int x,int y,int n)
    {
        int i,j;
        for(i=0; i<n ;i++)
        {
            if(arr.get(x).get(i).get(0)==n)
                return false;
            if(arr.get(i).get(y).get(0)==n)
                return false;
        }
        for(i=x-(x%3); i<x-(x%3)+3; i++)
        {
            for(j=y-(y%3); j<y-(y%3)+3; j++)
                if(arr.get(i).get(j).get(0)==n)
                    return false;
        }
        return true;
    }

    boolean valid()
    {
        Set row=new HashSet();
        Set col=new HashSet();
        Set block=new HashSet();
        int i,j,k,l;
        for(i=0 ;i<9 ;i++)
        {
            row.clear();
            col.clear();
            for(j=0; j<9; j++)
            {
                row.add(arr.get(i).get(j).get(0));
                col.add(arr.get(j).get(i).get(0));
            }
            if(row.size()!=9 || col.size()!=9)
                return false;
        }
        for(k=0; k<9; k+=3)
        {
            for(l=0; l<9; l+=3)
            {
                block.clear();
                for(i=k; i<k+3; i++)
                {
                    for(j=l; j<l+3; j++)
                    {
                        block.add(arr.get(i).get(j).get(0));
                    }
                }
                if(block.size()!=9)
                    return false;
            }
        }
        return true;
    }
    boolean bruteForce()
    {
        bf++;
        int i,j;
        int min=10,x=0,y=0;
        boolean flag;
        for(i=0; i<9; i++)
        {
            for(j=0; j<9; j++)
            {
                if(arr.get(i).get(j).size()<min && arr.get(i).get(j).get(0)==0)
                {
                    min=arr.get(i).get(j).size();
                    x=i;
                    y=j;
                }
            }
        }

        if(min==10)
            return false;
        List<List<List<Integer> > > currState= new ArrayList<List<List<Integer> > >();
        createArray(currState);
        copyState(currState, arr);
        for(i=1; i<arr.get(x).get(y).size(); i++)
        {
            Log.d("Setting : ", x + " " + y + " as " + arr.get(x).get(y).toString() + " to " + arr.get(x).get(y).get(i));
            arr.get(x).get(y).set(0, arr.get(x).get(y).get(i));
            flag=update(x,y);
            if(flag)
            {
                bruteForce();
                if(complete())
                    return true;
            }
            else
                Log.d("Message : ","Wrong assumption:");
            copyState(arr,currState);
        }
        currState.clear();
        return false;
    }

}
