package mastermind.view

import scalafx.scene.Parent
import scalafx.stage.{Popup, Stage}

trait RulesView:
  def exitButton(): Unit
  def showPopup(root: Parent): Unit

object RulesView:
  def apply(stage: Stage): RulesView = RulesViewImpl(stage)

  private class RulesViewImpl(stage: Stage) extends RulesView:
    private val popup = new Popup()

    def showPopup(root: Parent): Unit =
      popup.getContent.clear()
      popup.getContent.add(root)
      popup.show(stage)

    override def exitButton(): Unit =
      popup.hide()
