    import pandas as pd
    import numpy as np
    import time
    import watchdog
    from watchdog.observers import Observer
    from watchdog.events import FileSystemEventHandler
    from sklearn.feature_extraction.text import TfidfVectorizer
    from sklearn.metrics.pairwise import cosine_similarity
    import numpy as np
     
    data = pd.read_csv("ch3_data.csv")
     
    vectorizer = TfidfVectorizer()
    vectorizer.fit(data.values.ravel())
    CHeck="No"
    waitForYesOrNo = False
    question=""
    import filecmp
    import time
    import watchdog
    from watchdog.observers import Observer
    from watchdog.events import FileSystemEventHandler
    file = open("/home/mohamed/Desktop/inputData/input.txt","r") 
    lineNumber = 0
    class MyHandler(FileSystemEventHandler):
        def on_modified(self, event):
            print("Got it!")
            global CHeck
            global waitForYesOrNo
            global previosQuestion
            global question
            fileHandle = open ( '/home/mohamed/Desktop/inputData/input.txt',"r" )
            lineList = fileHandle.readlines()
            fileHandle.close()
            if waitForYesOrNo == True:  
                #question = [lineList[0]]
                waitForYesOrNo = False
                str = lineList[0]
                str = str.strip();
                if str == "yes" or str == "Yes" or str == "YES":
                    print("Enterd yes area")
                    print(question)
                    question = vectorizer.transform(question)
                    rank = cosine_similarity(question, vectorizer.transform(data['Questions'].values))
                    precent=np.amax(rank, axis=None, out=None) # Return Max Value In List
                    top3=np.argmax(rank)                       # Return Index Of Max Value
                    Output = data['Answers'].iloc[top3]
                    outputFile = open("output.txt" , "w+")
                    outputFile.write(Output)
                    outputFile.write('\n') 
                    print(Output) # Output
                else:
                    outputFile = open("output.txt" , "w+")
                    outputFile.write("USER_NO_ID");
                    outputFile.write('\n') 
     
            else:
                question = [lineList[0]]
                question = vectorizer.transform(question)
                CHeck="No"
                waitForYesOrNo = False
                rank = cosine_similarity(question, vectorizer
                                 .transform(data['Questions'].values))
     
     
                precent=np.amax(rank, axis=None, out=None) # Return Max Value In List
                top3=np.argmax(rank)                       # Return Index Of Max Value
                Output="" 
                if precent>=0.55: 
                    Output = data['Answers'].iloc[top3]
                    outputFile = open("output.txt" , "w+")
                    outputFile.write(Output)
                    outputFile.write('\n');  
                    print(Output) # Output
                else  :
                    Output = "Sorry I Can't I Understad you , Did You Mean,"+data['Questions'].iloc[top3]+"?\n Yes Or No"
                    waitForYesOrNo = True;
                    outputFile = open("output.txt" , "w+")
                    outputFile.write(Output)
                    outputFile.write('\n');  
                    print(Output) # Output
     
                    question = data['Questions'].iloc[top3] # Give Question That Model Predict If True
                    question = [question]
    event_handler = MyHandler()
    observer = Observer()
    observer.schedule(event_handler, path='/home/mohamed/Desktop/inputData', recursive=False)
    observer.start()
    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        observer.stop()
    observer.join()
     
    print("Closed")
