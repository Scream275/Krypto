# Read in all the functions
ReadFunctions<-function() {
	source("/home/roy/.git/Krypto/Krypto/Read_in_all_spectrum_files_of_a_directory.R")
	source("/home/roy/.git/Krypto/Krypto/Read_in_the_data_of_a_file_with_sep.R")
}

# Main program
ReadFunctions()
directory="/home/roy/.git/Krypto/Krypto/"
setwd(directory)
pattern<-"haeufigkeitsanalyse_cbc_[0-9]*"
dir<-readFolder(directory, TRUE, pattern)
data<-readData(dir)
names(data)<-c("buchstabe","haeufigkeit")

index<-order(data$buchstabe)
dataSorted<-data[index,]

png.file<-paste("haeufigkeitsanalyse_cbc", ".png")
png(file=png.file,height=1000, width=2000, res=100)
par(mfrow=c(1,1), mai=c(1,1,1,1))
plot(dataSorted$buchstabe, dataSorted$haeufigkeit, type="p", col="blue", lwd=2, 
	main="Haeufigkeitsanalyse", xlab="Buchstabe", font.main=4,cex.lab=1, cex.main=1, 
	cex.axis=1, font.axis=2, font.lab=2, lty=1, ylab="Haeufigkeit", ylim=c(0,5500))
#axis(2, labels=FALSE, font=2, at=seq(0, 200, by=10), tck=0.02, cex.axis=1)
#axis(2, labels=FALSE, font=2, at=seq(0, 200, by=5), tck=0.01, cex.axis=1)
#axis(2, labels=FALSE, font=2, at=seq(0, 200, by=1), tck=0.005, cex.axis=1)
#legend("topright",legend=legende,col=rainbow(length(plants)), lty=line, lwd=rep(2,length(plants)), bty="n", cex=1)
dev.off()
