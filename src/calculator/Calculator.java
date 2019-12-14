package calculator;

public class Calculator {

	public static void getCmd(String cmd)
	{
		if(number[2].equals("0で除算することはできません。"))
		{
			Init();
		}
		else {
			switch(cmd)
			{
			case"=":
				execute();
				break;
			case"CE":
				clearEntryFlag = true;;
				clearEntry();
				break;
			case"C":
				Init();
				break;
			case"←":
				try {
						StringBuilder sb = new StringBuilder(number[2]);
						sb.setLength(number[2].length() - 1);
						number[2] = sb.toString();
						if(number[2].length() == 0 || number[2].equals("-"))
						{
							clearEntryFlag = true;
							clearEntry();
						}
					}
				finally {
				}
				break;
			case"+/-":
				if(!number[2].equals("0"))
				{
					if('-' == number[2].charAt(0))
					{
						StringBuilder sb = new StringBuilder(number[2]);
						sb.deleteCharAt(0);
						number[2] = sb.toString();
					}
					else
					{
						StringBuilder sb = new StringBuilder(number[2]);
						sb.insert(0, "-");
						number[2] = sb.toString();
					}
				}
				break;
			case".":
				if(!number[2].contains("."))
				{
					number[2] = number[2] + cmd;
				}
				break;
			case"+":
			case"-":
			case"×":
			case"÷":
				clearEntryFlag = true;
				symbol = cmd;
				if(number[0].equals(""))
				{
					number[0] = roundDown(number[2]);
				}
				else
				{
					execute();
					symbol = cmd;
					number[0] = roundDown(number[2]);
				}
				break;
			case"0":
			case"1":
			case"2":
			case"3":
			case"4":
			case"5":
			case"6":
			case"7":
			case"8":
			case"9":
				setNumber(cmd);
				break;
			default:
				break;
			}
			lastCmd = cmd;
		}
	}

	public static String getFormula()
	{
		return number[0] + symbol + number[1];
	}

	public static String getAnswer()
	{
		return number[2];
	}

	// プライベートメソッド

	// 初期化
	private static void Init()
	{
		clearMemory();
		clearBuffer();
		clearEntryFlag = true;
		clearEntry();
	}

	// 入力欄のみの初期化
	private static void clearEntry()
	{
		if(clearEntryFlag)
		{
			number[2] = "0";
			clearEntryFlag = false;
		}
	}

	// 式出力のみの初期化
	private static void clearMemory()
	{
		for(int i = 0; i < number.length - 1; i++)
		{
				number[i] = "";
		}

		symbol = "";
	}

	// バッファの初期化
	private static void clearBuffer()
	{
		bufferNumber = "";
		bufferSymbol = "";
		lastCmd = "";
	}

	// 入力兼桁調整
	private static void setNumber(String cmd)
	{
		clearEntry();

		if(number[2].length() == 1 && number[2].contentEquals("0"))
		{
			number[2] = "";
		}

		int pos = number[2].indexOf(".");
		if(pos == -1)
		{
			if(number[2].length() < 16)
			{
				number[2] = number[2] + cmd;
			}
		}
		else
		{
			if(pos == 1 && number[2].charAt(0) == '0')
			{
				if(number[2].length() < 18)
				{
					number[2] = number[2] + cmd;
				}
			}
			else
			{
				if(number[2].length() < 17)
				{
					number[2] = number[2] + cmd;
				}
			}
		}
	}

	// 小数点以下すべて0のとき切り捨て
	private static String roundDown(String target)
	{
		String result = target;
		Boolean flag = true;

		int pos = result.indexOf(".");
		if(-1 != pos)
		{
			for(int i = pos + 1; i < result.length(); i++)
			{
				// 0以外があったならば切り捨てしない
				if(result.charAt(i) != '0')
				{
					flag = false;
				}
			}

			if(flag)
			{
				result = result.substring(0, pos);
			}
		}

		return result;
	}

	// 少数含むか含まないか
	private static Boolean checkType()
	{
		for(int i = 0; i < 2; i++)
		{
				number[i] = roundDown(number[i]);
				// 切り捨てメソッド後に小数点が残ってるならばdouble型で計算
				if(number[i].indexOf(".") != -1)
				{
					return true;
				}
		}

		return false;
	}

	// 計算実行
	private static void execute()
	{
		if (lastCmd.equals("=") && !bufferNumber.equals(""))
		{
			number[0] = number[2];
			number[1] = bufferNumber;
			symbol = bufferSymbol;
		}
		else if(!number[0].equals(""))
		{
			number[1] = number[2];
			bufferNumber = number[2];
			bufferSymbol = symbol;
		}

		if(!number[0].equals(""))
		{
			clearEntryFlag = true;

			Boolean isDouble = checkType();
			if(symbol.equals("÷")  && number[1].equals("0"))
			{
				Init();
				number[2] = "0で除算することはできません。";
			}
			else
			{
				if(isDouble)
				{
					Double x, y;
					x = Double.parseDouble(number[0]);
					y = Double.parseDouble(number[1]);
					switch(symbol)
					{
					case "+":
						number[2] = roundDown(Double.toString(x + y));
						break;
					case "-":
						number[2] = roundDown(Double.toString(x - y));
						break;
					case "×":
						number[2] = roundDown(Double.toString(x * y));
						break;
					case "÷":
						number[2] = roundDown(Double.toString(x / y));
						break;
					}
				}
				else
				{
					Long x, y;
					x = Long.parseLong(number[0]);
					y = Long.parseLong(number[1]);
					switch(symbol)
					{
					case "+":
						number[2] = roundDown(Long.toString(x + y));
						break;
					case "-":
						number[2] = roundDown(Long.toString(x - y));
						break;
					case "×":
						number[2] = roundDown(Long.toString(x * y));
						break;
					case "÷":
						number[2] = roundDown(Long.toString(x / y));
						break;
					}
				}
			}
			clearMemory();
		}
	}

	//プライベート変数
	private static String[] number = new String[3];
	private static String symbol;
	private static Boolean clearEntryFlag = false;

	private static String lastCmd = new String();
	private static String bufferNumber = new String();
	private static String bufferSymbol = new String();

	static {
		Init();
	}
}
