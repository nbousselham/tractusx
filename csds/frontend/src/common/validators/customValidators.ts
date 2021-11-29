// eslint-disable-next-line max-len
const ISODateStringRegex =
  /^(-?(?:[1-9][0-9]*)?[0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])T(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(.[0-9]+)?(Z)?$/;
/**
 * AlphaNumeric validator with possible dash and underscore
 * @param {string} value Like-this_123
 * @returns {boolean}
 */
export const numericDashed = (value: string): boolean => /^[\d-]*$/.test(value);
export const alphaNumericDashed = (value: string) => /^[A-z\d_-]*$/.test(value);
export const alphaDashed = (value: string) => /^[A-z_-]*$/.test(value);
export const wordChars = (value: string) => /^[A-zäöüÄÖÜß\s-]*$/.test(value);
export const wordCharsNumeric = (value: string) => /^[A-zäöüÄÖÜß\s-\d]*$/.test(value);
export const wordCharsNumericDotted = (value: string) => /^[.A-zäöüÄÖÜß\s-\d]*$/.test(value);
export const atLeastOneLowerCase = (value: string) => /[a-z]/.test(value);
export const atLeastOneUpperCase = (value: string) => /[A-Z]/.test(value);
export const atLeastOneNumber = (value: string) => /\d/.test(value);
export const atLeastOneSpecialCharacter = (value: string) => /[!#$%()*+,-./:;=?@\\^_`{|}~]/.test(value);
export const exceptForbiddenCharacters = (value: string) => /^[^<>&"'[\]]*$/.test(value);
export const forbidAdmin = (value: string) => !/admin/.test(value);
export const isISODateString = (value: string) => ISODateStringRegex.test(value);
