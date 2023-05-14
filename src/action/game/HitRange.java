package action.game;

public class HitRange {
	public HitLine[] hitLines;

	public HitRange(HitLine[] hitLines) {
		super();
		this.hitLines = hitLines;
	}
	
	public static final HitLine upLine = new HitLine(0, 48, 48, 48);
	public static final HitLine downLine = new HitLine(48, 0, 0, 0);
	public static final HitLine leftLine = new HitLine(0, 0, 0, 48);
	public static final HitLine rightLine = new HitLine(48, 48, 48, 0);
	public static final HitLine mostLeftLine = new HitLine(0, 720, 0, 0);
	public static final HitLine rightupLine = new HitLine(0, 0, 48, 48);
	public static final HitLine leftupLine = new HitLine(0, 48, 48, 0);
	public static final HitLine rightdownLine = new HitLine(48, 0, 0, 48);
	public static final HitLine leftdownLine = new HitLine(48, 48, 0, 0);
	public static final HitLine rightupLineA = new HitLine(0, 0, 48, 24);
	public static final HitLine rightupLineB = new HitLine(0, 24, 48, 48);
	public static final HitLine leftupLineA = new HitLine(0, 48, 48, 24);
	public static final HitLine leftupLineB = new HitLine(0, 24, 48, 0);
	public static final HitLine rightdownLineA = new HitLine(48, 24, 0, 48);
	public static final HitLine rightdownLineB = new HitLine(48, 0, 0, 24);
	public static final HitLine leftdownLineA = new HitLine(48, 24, 0, 0);
	public static final HitLine leftdownLineB = new HitLine(48, 48, 0, 24);
	
	public static final HitRange[] hitRanges = {
			new HitRange(new HitLine[] {}),
			new HitRange(new HitLine[] {upLine}),
			new HitRange(new HitLine[] {rightLine}),
			new HitRange(new HitLine[] {downLine}),
			new HitRange(new HitLine[] {leftLine}),
			new HitRange(new HitLine[] {upLine, rightLine}),
			new HitRange(new HitLine[] {upLine, downLine}),
			new HitRange(new HitLine[] {upLine, leftLine}),
			new HitRange(new HitLine[] {rightLine, downLine}),
			new HitRange(new HitLine[] {rightLine, leftLine}),
			new HitRange(new HitLine[] {downLine, leftLine}), // 10
			new HitRange(new HitLine[] {upLine, rightLine, downLine}),
			new HitRange(new HitLine[] {upLine, rightLine, leftLine}),
			new HitRange(new HitLine[] {upLine, downLine, leftLine}),
			new HitRange(new HitLine[] {rightLine, downLine, leftLine}),
			new HitRange(new HitLine[] {upLine, rightLine, downLine, leftLine}),
			new HitRange(new HitLine[] {rightupLine}),
			new HitRange(new HitLine[] {leftupLine}),
			new HitRange(new HitLine[] {rightdownLine}),
			new HitRange(new HitLine[] {leftdownLine}),
			new HitRange(new HitLine[] {rightupLineA}),
			new HitRange(new HitLine[] {rightupLineB}),
			new HitRange(new HitLine[] {leftupLineA}),
			new HitRange(new HitLine[] {leftupLineB}),
			new HitRange(new HitLine[] {rightdownLineA}),
			new HitRange(new HitLine[] {rightdownLineB}),
			new HitRange(new HitLine[] {leftdownLineA}),
			new HitRange(new HitLine[] {leftdownLineB})


	};
	
	public static final HitRange up40 = new HitRange(new HitLine[] {new HitLine(0, 40, 40, 40)});
	
	public static final HitRange range40 = new HitRange(new HitLine[] {new HitLine(0, 40, 40, 40),
			new HitLine(40, 0, 0, 0), new HitLine(0, 0, 0, 40), new HitLine(40, 40, 40, 0)});
}
