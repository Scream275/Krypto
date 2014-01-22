# Read the data from the file
readData<-function(filename) {
	data<-read.table(dec=",", file=filename, header = FALSE, sep=";")
	return(data)
}