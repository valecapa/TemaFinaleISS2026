### conda install diagrams
from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted'
}
evattr = {
    'color': 'darkgreen',
    'style': 'dotted'
}
with Diagram('sprint3Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxcargoservice', graph_attr=nodeattr):
          cargoservice=Custom('cargoservice','./qakicons/symActorWithobjSmall.png')
          cargorobot=Custom('cargorobot','./qakicons/symActorWithobjSmall.png')
          ioport=Custom('ioport','./qakicons/symActorWithobjSmall.png')
          sonar=Custom('sonar','./qakicons/symActorWithobjSmall.png')
     with Cluster('ctxrobotsmart', graph_attr=nodeattr):
          robotsmart=Custom('robotsmart(ext)','./qakicons/externalQActor.png')
     sys >> Edge( label='sonarreading', **evattr, decorate='true', fontcolor='darkgreen') >> sonar
     ioport >> Edge(color='magenta', style='solid', decorate='true', label='<loadrequest<font color="darkgreen"> loadaccepted loadrejected</font> &nbsp; >',  fontcolor='magenta') >> cargoservice
     cargorobot >> Edge(color='magenta', style='solid', decorate='true', label='<moverobot<font color="darkgreen"> moverobotdone moverobotfailed</font> &nbsp; >',  fontcolor='magenta') >> robotsmart
     cargoservice >> Edge(color='magenta', style='solid', decorate='true', label='<transportContainer<font color="darkgreen"> transportDone transportFailed</font> &nbsp; >',  fontcolor='magenta') >> cargorobot
     cargoservice >> Edge(color='blue', style='solid',  decorate='true', label='<blinkLed &nbsp; >',  fontcolor='blue') >> sonar
     sonar >> Edge(color='blue', style='solid',  decorate='true', label='<sonarfault &nbsp; sonarrestored &nbsp; >',  fontcolor='blue') >> cargoservice
     cargoservice >> Edge(color='blue', style='solid',  decorate='true', label='<updateDisplay &nbsp; >',  fontcolor='blue') >> ioport
     cargorobot >> Edge(color='blue', style='solid',  decorate='true', label='<robotHalted &nbsp; >',  fontcolor='blue') >> cargoservice
     sonar >> Edge(color='blue', style='solid',  decorate='true', label='<setOccupied &nbsp; >',  fontcolor='blue') >> ioport
     sonar >> Edge(color='blue', style='solid',  decorate='true', label='<sonarfault &nbsp; sonarrestored &nbsp; >',  fontcolor='blue') >> cargorobot
     cargorobot >> Edge(color='blue', style='solid',  decorate='true', label='<halt &nbsp; >',  fontcolor='blue') >> robotsmart
diag
