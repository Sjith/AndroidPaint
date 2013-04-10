package ca.qc.johnabbott.cs603.asg3;

import ca.qc.johnabbott.cs603.R;

import ca.qc.johnabbott.cs603.asg3.shapes.Ellipse;
import ca.qc.johnabbott.cs603.asg3.shapes.Line;/*
import ca.qc.johnabbott.cs603.asg3.shapes.Bezier;
import ca.qc.johnabbott.cs603.asg3.shapes.PolyGon;
import ca.qc.johnabbott.cs603.asg3.shapes.PolyLine;*/
import ca.qc.johnabbott.cs603.asg3.shapes.Rectangle;
import ca.qc.johnabbott.cs603.asg3.shapes.Shape;
import ca.qc.johnabbott.cs603.asg3.tools.LineTool;
import ca.qc.johnabbott.cs603.asg3.tools.Tool;
import ca.qc.johnabbott.cs603.asg3.tools.ToolName;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ToolSettingsDialog extends Dialog {

	private ToolBox toolbox;
	private Paint previewPaint;
	
	/* Interface elements */
	private RadioButton radioRectangle;
	private RadioButton radioEllipse;
	private RadioButton radioLine;
	private RadioButton radioPath; 
	private RadioButton radioCurve;
	private SeekBar seekBarWidth;
	private ImageButton buttonStrokeColor;
	private ImageButton buttonFillColor;
	
	public ToolSettingsDialog(Context context, ToolBox tbox) {
		super(context);
		this.setContentView(R.layout.dialog_tools);
		this.setTitle(R.string.tools_dialog_title);
		this.setCanceledOnTouchOutside(true);

		this.toolbox = tbox;
		
		this.previewPaint = new Paint();
		this.previewPaint.setAntiAlias(true);
		this.previewPaint.setStrokeCap(Paint.Cap.ROUND);
		
		radioRectangle = (RadioButton) findViewById(R.id.radioRectangle);
		radioRectangle.setOnClickListener(new ToolClick(ToolName.RECTANGLE));
		
		radioEllipse = (RadioButton) findViewById(R.id.radioEllipse);
		radioEllipse.setOnClickListener(new ToolClick(ToolName.ELLIPSE));
		
		radioLine = (RadioButton) findViewById(R.id.radioLine);
		radioLine.setOnClickListener(new ToolClick(ToolName.LINE));
		
		radioCurve = (RadioButton) findViewById(R.id.radioCurve);
		radioCurve.setOnClickListener(new ToolClick(ToolName.CURVE));
		
		radioPath = (RadioButton) findViewById(R.id.radioPath);
		radioPath.setOnClickListener(new ToolClick(ToolName.PATH));
		
		// default
		switch(toolbox.getCurrentToolName()) {
			case RECTANGLE:
				radioRectangle.setChecked(true); break;
			case ELLIPSE:
				radioEllipse.setChecked(true); break;
			case CURVE:
				radioCurve.setChecked(true); break;
			case PATH:
				radioPath.setChecked(true); break;
			case LINE:
			default:
				radioLine.setChecked(true); break;
		}
		
		seekBarWidth = (SeekBar)this.findViewById(R.id.widthSeekBar);
		seekBarWidth.setProgress(toolbox.getStrokeWidth());
		seekBarWidth.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {			
			@Override
			public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
				toolbox.setStrokeWidth(progress);
				updatePreview();
			}
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {}
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {}			
		});
				
		buttonStrokeColor = (ImageButton) findViewById(R.id.btnFG);
		buttonStrokeColor.setBackgroundColor(toolbox.getStrokeColor());
		buttonStrokeColor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				final ColorChooserDialog dialog = new ColorChooserDialog(getContext(), toolbox.getStrokeColor());
				dialog.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						toolbox.setStrokeColor(dialog.getColor());
						buttonStrokeColor.setBackgroundColor(dialog.getColor());
						updatePreview();
					}
				});
				dialog.show();
			}
		});
		
		
		buttonFillColor = (ImageButton) findViewById(R.id.btnBG);
		buttonFillColor.setBackgroundColor(toolbox.getFillColor());
		buttonFillColor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				final ColorChooserDialog dialog = new ColorChooserDialog(getContext(), toolbox.getFillColor());
				dialog.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						toolbox.setFillColor(dialog.getColor());
						buttonFillColor.setBackgroundColor(dialog.getColor());
						updatePreview();
					}
				});
				dialog.show();		
			}
		});
		
		Button done = (Button)this.findViewById(R.id.widthDone);
		done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
	
		updatePreview();
		this.show();
	}

	private void updatePreview() {		
		//previewPaint = toolbox.getPreviewPaint();
		//toolbox.getCurrentTool().drawPreview(canvas);

		// white background
		Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
		bitmap.eraseColor(Color.LTGRAY);
				
		Canvas canvas = new Canvas();
		canvas.setBitmap(bitmap);
		
		
		
		// Write Canvas to Screen (element in dialog)
		ImageView image = (ImageView)findViewById(R.id.widthImageView);
		image.draw(canvas);
	}
	
	private class ToolClick implements View.OnClickListener {
		private ToolName name;
	
		public ToolClick(ToolName name) {
			this.name = name;
		}
		@Override
		public void onClick(View view) {
			toolbox.changeTool(name);
			updatePreview();
		}
	}
}
