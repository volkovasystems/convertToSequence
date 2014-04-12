import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.Stack;

public class convertToSequence{
	private static final String DEFAULT_SEPARATOR = ",";
	private static final String EMPTY_STRING = "";

	public static void main( String... parameters ){
		try{
			String separator = parameters[ 0 ];
			String sequenceIndex = parameters[ 1 ];
			String dictionary = EMPTY_STRING;

			if( parameters.length == 3 ){
				dictionary = parameters[ 2 ];
			}else if( parameters.length == 2 ){
				separator = EMPTY_STRING;
				sequenceIndex = parameters[ 0 ];
				dictionary = parameters[ 1 ];
			}

			String sequence = convertToSequence( separator, sequenceIndex, dictionary );
			System.out.print( sequence );
		}catch( Exception exception ){
			System.err.print( exception.getMessage( ) );
		}
	}

	public static String convertToSequence( String separator, String sequenceIndex, String dictionary ){
		if( separator == null || separator == EMPTY_STRING ){
			separator = DEFAULT_SEPARATOR;
		}

		/*
			We need to split the dictionary
				so that we can use the capabilities of arrays in java.
		*/
		String dictionaryList[ ] = null;
		if( contains( dictionary, Pattern.compile( separator ) ) ){
			dictionaryList = dictionary.split( separator );
		}else{
			/*
				If we can't find any separator then separate
					them by empty spaces.
			*/
			dictionaryList = dictionary.split( EMPTY_STRING );

			/*
				We are doing this because there's an extra 
					null element when we split by empty string.
			*/
			dictionaryList = Arrays.copyOfRange( dictionaryList, 1, dictionaryList.length );
		}

		Integer dictionarySequenceLength = dictionaryList.length;
		BigInteger dictionaryLength = new BigInteger( dictionarySequenceLength.toString( ) );
		int lastIndex = dictionarySequenceLength - 1;

		BigInteger index = new BigInteger( sequenceIndex );
		BigInteger remainder;

		Stack<String> sequenceStack = new Stack<>( );
		do{
			remainder = index.mod( dictionaryLength );
			if( remainder.compareTo( BigInteger.ZERO ) != 0 ){
				sequenceStack.push( dictionaryList[ remainder.intValue( ) - 1 ] );
			}else if( remainder.compareTo( BigInteger.ZERO ) == 0 ){
				sequenceStack.push( dictionaryList[ lastIndex ] );
			}
			index = index.divide( dictionaryLength );
		}while( index.compareTo( BigInteger.ZERO ) != 0 );

		String sequenceList[ ] = sequenceStack.toArray( ( new String[ ]{ } ) );
		String sequence = Arrays.toString( sequenceList ).replaceAll( ", ", separator ).replaceAll( "\\[|\\]|\\s", "" );
		return sequence;
	}

	/*
		This will check if the pattern is found in the string.
		This will be used for finding the separator in the given sequence.
		This is made private and static so that it will not be even
			accessible using reflections.
	*/
	private static boolean contains( String string, Pattern pattern ){
		Matcher matcher = pattern.matcher( string );
		int matchCount = 0;
		while( matcher.find( ) ){
			matchCount++;
			if( matchCount > 0 ){
				return true;
			}
		}
		return false;
	}
}