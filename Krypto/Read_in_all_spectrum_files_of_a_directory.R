# Read in all the spectrum files of a directory
readFolder<-function(filename, boolean, pattern) {
	return(list.files(path=filename, pattern=pattern, all.files=FALSE, full.names=boolean,
			recursive=FALSE, include.dirs=FALSE))
}