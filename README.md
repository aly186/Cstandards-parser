# Cstandards-parser
Introduction</h1>
<p>This project is a toolkit for C language. It adds new standards for the code by specifying new spelling for declarations. <br  />
<br  />
 The declaration standards for the global, local and function declaration are stored in <b>Cstandards.json</b> which are written as regex. The visitors function is used to visit the parse tree to get the declared words and its type from the input code. Then our function compares the declared word spelling according to its type, with the regex in Cstandards.json. If it doesn’t match the regex an error message will be displayed inquiring to follow the given expression standards.
