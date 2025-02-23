export function extractValue(text, key) {
  key = `"${key}":`;
  const keyIndex = text.indexOf(key);
  if (keyIndex !== -1) {
    const startQuoteIndex = text.indexOf('"', keyIndex + key.length);
    const endQuoteIndex = text.indexOf('"', startQuoteIndex + 1);
    if (startQuoteIndex !== -1 && endQuoteIndex !== -1) {
      const extractedVal = text.substring(startQuoteIndex + 1, endQuoteIndex);
      console.log("Extracted extractedVal:", extractedVal);
      return extractedVal;
    } else {
      console.log(`Could not locate the closing quote for ${key}.`);
    }
  } else {
    console.log(`Key ${key} not found in the text.`);
  }
}

export function extractAndSetGlobal(text, key, varName) {
  key = `"${key}":`;
  const keyIndex = text.indexOf(key);
  if (keyIndex !== -1) {
    const startQuoteIndex = text.indexOf('"', keyIndex + key.length);
    const endQuoteIndex = text.indexOf('"', startQuoteIndex + 1);
    if (startQuoteIndex !== -1 && endQuoteIndex !== -1) {
      const extractedVal = text.substring(startQuoteIndex + 1, endQuoteIndex);
      const val = client.global.get(extractedVal);
      console.log("Extracted " + varName + " : ", extractedVal,
          " setting value to: ",
          val);
      client.global.set(varName, val);
      return extractedVal;
    } else {
      console.log(`Could not locate the closing quote for ${key}.`);
    }
  } else {
    console.log(`Key ${key} not found in the text.`);
  }

}

