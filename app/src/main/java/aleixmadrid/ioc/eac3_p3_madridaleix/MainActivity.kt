package aleixmadrid.ioc.eac3_p3_madridaleix

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private val animationDuration = 3000L

    private lateinit var finalSet: AnimatorSet
    private lateinit var ullHomerImageView: ImageView
    private lateinit var donutImageView: ImageView
    private lateinit var homerSipsonsView: ImageView
    private lateinit var engBlau:ImageView
    private lateinit var engVermell:ImageView
    private lateinit var engVerd:ImageView
    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying = false
    private var visible = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()

        // Animar la capçalera
        val titleSimpsons = findViewById<ImageView>(R.id.titleSimpsons)
        val animTitle = titleSimpsons.background as AnimationDrawable
        animTitle.start()

        titleSimpsons.setOnClickListener {
            if (visible){
                hideViews()
                stopAnimation()
                visible=false
            } else {
                showViews()
                startGearsAnimation()
                startAnimation()
                visible=true
            }
        }

        // Configurar reproducció d'àudio
        mediaPlayer = MediaPlayer.create(this, R.raw.the_simpsons)
        donutImageView.setOnClickListener {
            toggleAudio()
        }
    }


    private fun initializeViews(){

        ullHomerImageView = findViewById(R.id.imgUllHomer)
        donutImageView = findViewById(R.id.imgDonut)
        homerSipsonsView = findViewById(R.id.imgHomer)
        engBlau = findViewById(R.id.ivEngBlau)
        engVermell = findViewById(R.id.ivEngVermell)
        engVerd = findViewById(R.id.ivEngVerd)

    }



    private fun hideViews(){

        ullHomerImageView.visibility = View.INVISIBLE
        donutImageView.visibility = View.INVISIBLE
        engBlau.visibility = View.INVISIBLE
        engVerd.visibility = View.INVISIBLE
        engVermell.visibility = View.INVISIBLE

    }


    private fun showViews(){

        ullHomerImageView.visibility = View.VISIBLE
        donutImageView.visibility = View.VISIBLE
        engBlau.visibility = View.VISIBLE
        engVerd.visibility = View.VISIBLE
        engVermell.visibility = View.VISIBLE

    }

    private fun startAnimation(){

        // Crear i donar valors a les animacions
        val moveDown = ValueAnimator.ofFloat(0f, 750.0f)
        moveDown.duration = animationDuration
        moveDown.addUpdateListener {
               donutImageView.translationY = it.animatedValue as Float
        }

        val moveRight = ValueAnimator.ofFloat(0f, 50.0f)
        moveRight.duration = animationDuration
        moveRight.addUpdateListener {
            donutImageView.translationX = it.animatedValue as Float
        }

        val moveUp = ValueAnimator.ofFloat(750.0f, 0f)
        moveUp.duration = animationDuration
        moveUp.addUpdateListener {
            donutImageView.translationY = it.animatedValue as Float
        }

        val moveLeft = ValueAnimator.ofFloat(50.0f, 0f)
        moveLeft.duration = animationDuration
        moveLeft.addUpdateListener {
            donutImageView.translationX = it.animatedValue as Float
        }

        val rotateUllDown = ValueAnimator.ofFloat(50f, -60f)
        rotateUllDown.duration = animationDuration
        rotateUllDown.addUpdateListener {
            val value = it.animatedValue as Float
            ullHomerImageView.rotation = value
        }

        val rotateUllUp = ValueAnimator.ofFloat(-60f, 50f)
        rotateUllUp.duration = animationDuration
        rotateUllUp.addUpdateListener {
            val value = it.animatedValue as Float
            ullHomerImageView.rotation = value
        }

        // Crear conjunts d'animacions
        val firstSet = AnimatorSet()
        firstSet.playTogether(moveDown, moveRight, rotateUllDown)

        val secondSet = AnimatorSet()
        secondSet.playTogether(moveUp, moveLeft, rotateUllUp)

        finalSet = AnimatorSet()
        finalSet.playSequentially(firstSet, secondSet)
        finalSet.interpolator = LinearInterpolator()

        // Configurar el listener per a repetir l'animació
        finalSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                // Reiniciar l'animació al finalitzar
                finalSet.start()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })

            finalSet.start()
    }

    private fun startGearsAnimation() {
        // Iniciar l'animació de rotació als engranatges de colors
        startRotationAnimation(engBlau, animationDuration)
        startRotationAnimation(engVermell, animationDuration)
        startRotationAnimation(engVerd, animationDuration)
    }

    private fun startRotationAnimation(imageView: ImageView, duration: Long) {
        val animator = ValueAnimator.ofFloat(0f, 360f)
        animator.duration = duration
        animator.repeatCount = ValueAnimator.INFINITE
        animator.interpolator = LinearInterpolator()

        // Direcció aleatoria per la primera animació
        val randomDirection = if (Random.nextBoolean()) 1 else -1
        imageView.rotation = Random.nextFloat() * 360
        animator.setFloatValues(imageView.rotation, imageView.rotation + 360 * randomDirection)

        //Actualitzar a cada frame la rotació de l'ImageView
        animator.addUpdateListener { valueAnimator ->
            imageView.rotation = valueAnimator.animatedValue as Float
        }

        // Establir un listener per al canvi aleatori de sentit de rotació a cada repetició.
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {
                val randomDirection = if (Random.nextBoolean()) 1 else -1
                animator.setFloatValues(imageView.rotation, imageView.rotation + 360 * randomDirection)
            }
        })
        // Iniciar l'animació
        animator.start()
    }

    private fun stopAnimation(){
        finalSet.end()
    }

    // Controlar la reproducció de l'àudio
    private fun toggleAudio() {
        if (isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
        isPlaying = !isPlaying
    }
}
